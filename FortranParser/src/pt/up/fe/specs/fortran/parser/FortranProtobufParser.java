package pt.up.fe.specs.fortran.parser;

import flang_dumper.protocol.AstStream;
import pt.up.fe.specs.fortran.ast.FortranContext;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Reads the framed binary AST stream produced by flang-dumper. */
public final class FortranProtobufParser {

    private static final byte[] MAGIC = "FLASTPB1".getBytes(java.nio.charset.StandardCharsets.US_ASCII);
    private static final int HEADER_SIZE = 12;
    private static final long PROTOCOL_VERSION = 1;
    private static final long MAX_RECORD_SIZE = 64L * 1024 * 1024;

    private final FortranContext context;
    private final Map<Long, String> nodeIds;
    private final Set<Long> referencedIds;
    private final Set<String> ids;
    private final Map<String, Map<String, Object>> attributes;
    private final Map<String, FortranNode> fortranNodes;
    private final List<CommentData> comments;
    private String firstNode;
    private boolean sawRecord;

    private FortranProtobufParser(FortranContext context) {
        this.context = context;
        this.nodeIds = new HashMap<>();
        this.referencedIds = new HashSet<>();
        this.ids = new LinkedHashSet<>();
        this.attributes = new LinkedHashMap<>();
        this.fortranNodes = new LinkedHashMap<>();
        this.comments = new ArrayList<>();
    }

    public static FortranJsonResult parse(InputStream input, FortranContext context) {
        try {
            return new FortranProtobufParser(context).parseStream(input);
        } catch (IOException e) {
            throw new RuntimeException("Problem while parsing Fortran protobuf", e);
        }
    }

    private FortranJsonResult parseStream(InputStream input) throws IOException {
        readHeader(input);

        while (true) {
            AstStream.StreamRecord record = readRecord(input);
            if (record == null) {
                break;
            }

            if (!sawRecord) {
                if (record.getRecordCase() != AstStream.StreamRecord.RecordCase.NODE) {
                    throw protocolError("first stream record must be a node");
                }
                sawRecord = true;
            }

            switch (record.getRecordCase()) {
                case NODE -> processNode(record.getNode());
                case COMMENT -> processComment(record.getComment());
                case ENUM_CATALOG -> validateEnumCatalog(record.getEnumCatalog());
                case RECORD_NOT_SET -> throw protocolError("stream record oneof must be set");
            }
        }

        if (nodeIds.isEmpty()) {
            throw protocolError("AST stream must contain at least one node");
        }

        resolveReferences();
        applyComments();

        return new FortranJsonResult(context, firstNode, ids, fortranNodes, FlangData.convert(attributes));
    }

    private void processNode(AstStream.NodeRecord node) throws IOException {
        long nodeId = node.getNodeId();
        if (nodeId == 0) {
            throw protocolError("node ID must be nonzero");
        }
        if (nodeIds.containsKey(nodeId)) {
            throw protocolError("duplicate node ID " + Long.toUnsignedString(nodeId));
        }
        if (node.getKindId() == 0) {
            throw protocolError("node kind ID must be nonzero");
        }
        if (node.getKindName().isEmpty()) {
            throw protocolError("node kind name must be nonempty");
        }

        String id = "0x" + Long.toUnsignedString(nodeId, 16) + "-" + node.getKindName();
        nodeIds.put(nodeId, id);
        ids.add(id);
        if (firstNode == null) {
            firstNode = id;
        }

        Map<String, Object> nodeAttributes = new LinkedHashMap<>();
        nodeAttributes.put("id", id);
        attributes.put(id, nodeAttributes);

        FlangToClass.getClass(node.getKindName()).ifPresent(fortranClass ->
                fortranNodes.put(id, context.get(FortranContext.FACTORY)
                        .newNode(fortranClass, List.of(), id)));

        for (int index = 0; index < node.getAttributesCount(); index++) {
            AstStream.Attribute attribute = node.getAttributes(index);
            if (attribute.getKey().isEmpty()) {
                throw protocolError("node attribute key must be nonempty");
            }

            Object value = convertAttributeValue(attribute.getValue(),
                    "node " + id + " attribute '" + attribute.getKey() + "'");
            // Map.put intentionally gives duplicate keys their legacy last-wins behavior.
            nodeAttributes.put(attribute.getKey(), value);
        }
    }

    private Object convertAttributeValue(AstStream.AttributeValue value, String where) throws IOException {
        return switch (value.getValueCase()) {
            case STRING_VALUE -> value.getStringValue();
            case INTEGER_VALUE -> Long.toString(value.getIntegerValue());
            case BOOL_VALUE -> value.getBoolValue() ? "1" : "0";
            case NODE_REFERENCE -> nodeReference(value.getNodeReference(), where);
            case LIST_VALUE -> convertList(value.getListValue(), where);
            case NULL_VALUE -> null;
            case DOUBLE_VALUE -> Double.toString(value.getDoubleValue());
            case BYTES_VALUE -> throw protocolError(where + " uses bytes, which has no legacy JSON mapping");
            case UINT64_VALUE -> Long.toUnsignedString(value.getUint64Value());
            case VALUE_NOT_SET -> throw protocolError(where + " value oneof must be set");
        };
    }

    private Object convertValue(AstStream.Value value, String where) throws IOException {
        return switch (value.getValueCase()) {
            case STRING_VALUE -> value.getStringValue();
            case INTEGER_VALUE -> Long.toString(value.getIntegerValue());
            case BOOL_VALUE -> value.getBoolValue() ? "1" : "0";
            case NODE_REFERENCE -> nodeReference(value.getNodeReference(), where);
            case LIST_VALUE -> convertList(value.getListValue(), where);
            case NULL_VALUE -> null;
            case DOUBLE_VALUE -> Double.toString(value.getDoubleValue());
            case BYTES_VALUE -> throw protocolError(where + " uses bytes, which has no legacy JSON mapping");
            case UINT64_VALUE -> Long.toUnsignedString(value.getUint64Value());
            case VALUE_NOT_SET -> throw protocolError(where + " value oneof must be set");
        };
    }

    private Object nodeReference(long nodeId, String where) throws IOException {
        if (nodeId == 0) {
            throw protocolError(where + " has a zero node reference");
        }
        referencedIds.add(nodeId);
        return new NodeReference(nodeId);
    }

    private List<Object> convertList(AstStream.ValueList valueList, String where) throws IOException {
        List<Object> values = new ArrayList<>(valueList.getItemsCount());
        for (int index = 0; index < valueList.getItemsCount(); index++) {
            values.add(convertValue(valueList.getItems(index), where + "[" + index + "]"));
        }
        return values;
    }

    private void processComment(AstStream.CommentRecord comment) throws IOException {
        long stmtNodeId = comment.getStmtNodeId();
        if (stmtNodeId == 0) {
            throw protocolError("comment statement node ID must be nonzero");
        }
        referencedIds.add(stmtNodeId);
        comments.add(new CommentData(comment.getText(), stmtNodeId, comment.getTrailing()));
    }

    private void validateEnumCatalog(AstStream.EnumCatalog catalog) throws IOException {
        if (catalog.getEnumTypeName().isEmpty()) {
            throw protocolError("enum catalog type name must be nonempty");
        }

        Set<String> names = new HashSet<>();
        for (AstStream.EnumEntry entry : catalog.getEntriesList()) {
            if (entry.getName().isEmpty()) {
                throw protocolError("enum entry name must be nonempty");
            }
            if (!names.add(entry.getName())) {
                throw protocolError("duplicate enum entry name '" + entry.getName() + "'");
            }
        }
    }

    private void resolveReferences() throws IOException {
        for (long referencedId : referencedIds) {
            if (!nodeIds.containsKey(referencedId)) {
                throw protocolError("stream contains a reference to missing node ID "
                        + Long.toUnsignedString(referencedId));
            }
        }

        for (Map<String, Object> nodeAttributes : attributes.values()) {
            for (Map.Entry<String, Object> attribute : nodeAttributes.entrySet()) {
                attribute.setValue(resolveValue(attribute.getValue()));
            }
        }
    }

    private Object resolveValue(Object value) {
        if (value instanceof NodeReference reference) {
            return nodeIds.get(reference.nodeId());
        }
        if (value instanceof List<?> list) {
            List<Object> resolved = new ArrayList<>(list.size());
            for (Object item : list) {
                resolved.add(resolveValue(item));
            }
            return resolved;
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    private void applyComments() throws IOException {
        for (CommentData comment : comments) {
            String stmtId = nodeIds.get(comment.stmtNodeId());
            Map<String, Object> stmtAttributes = attributes.get(stmtId);
            if (stmtAttributes == null) {
                throw protocolError("comment references missing node ID "
                        + Long.toUnsignedString(comment.stmtNodeId()));
            }

            if (!comment.trailing()) {
                stmtAttributes.putIfAbsent("leadingComments", new ArrayList<>());
                List<String> leadingComments = (List<String>) stmtAttributes.get("leadingComments");
                leadingComments.add(comment.text());
            } else {
                stmtAttributes.put("trailingComment", comment.text());
            }
        }
    }

    private AstStream.StreamRecord readRecord(InputStream input) throws IOException {
        int firstByte = input.read();
        if (firstByte == -1) {
            return null;
        }

        long length = readVarint32(input, firstByte);
        if (length == 0) {
            throw protocolError("zero-length protobuf records are not allowed");
        }
        if (length > MAX_RECORD_SIZE) {
            throw protocolError("protobuf record exceeds 64 MiB maximum");
        }

        byte[] payload = new byte[(int) length];
        readFully(input, payload, 0, payload.length, "protobuf record payload");
        try {
            return AstStream.StreamRecord.parseFrom(payload);
        } catch (IOException e) {
            throw protocolError("protobuf parsing failed", e);
        }
    }

    private static long readVarint32(InputStream input, int firstByte) throws IOException {
        long value = 0;
        int currentByte = firstByte;

        for (int index = 0; index < 5; index++) {
            if (index != 0) {
                currentByte = input.read();
                if (currentByte == -1) {
                    throw protocolError("truncated record length varint");
                }
            }

            if (index == 4 && (currentByte & 0xf0) != 0) {
                throw protocolError("record length varint overflows uint32");
            }

            value |= (long) (currentByte & 0x7f) << (7 * index);
            if ((currentByte & 0x80) == 0) {
                if (encodedVarintSize(value) != index + 1) {
                    throw protocolError("record length varint is not canonical");
                }
                return value;
            }
        }

        throw protocolError("record length varint is too long");
    }

    private static int encodedVarintSize(long value) {
        int size = 1;
        while (value >= 0x80) {
            value >>>= 7;
            size++;
        }
        return size;
    }

    private static void readHeader(InputStream input) throws IOException {
        byte[] header = new byte[HEADER_SIZE];
        readFully(input, header, 0, header.length, "protocol header");

        for (int index = 0; index < MAGIC.length; index++) {
            if (header[index] != MAGIC[index]) {
                throw protocolError("invalid protocol magic");
            }
        }

        long version = (header[8] & 0xffL)
                | ((header[9] & 0xffL) << 8)
                | ((header[10] & 0xffL) << 16)
                | ((header[11] & 0xffL) << 24);
        if (version != PROTOCOL_VERSION) {
            throw protocolError("unsupported protocol version: " + version);
        }
    }

    private static void readFully(InputStream input, byte[] bytes, int offset, int length,
                                  String context) throws IOException {
        int readTotal = 0;
        while (readTotal < length) {
            int read = input.read(bytes, offset + readTotal, length - readTotal);
            if (read == -1) {
                throw protocolError("truncated " + context);
            }
            if (read == 0) {
                int nextByte = input.read();
                if (nextByte == -1) {
                    throw protocolError("truncated " + context);
                }
                bytes[offset + readTotal] = (byte) nextByte;
                readTotal++;
                continue;
            }
            readTotal += read;
        }
    }

    private static IOException protocolError(String message) {
        return new IOException(message);
    }

    private static IOException protocolError(String message, Throwable cause) {
        return new IOException(message, cause);
    }

    private record NodeReference(long nodeId) {
    }

    private record CommentData(String text, long stmtNodeId, boolean trailing) {
    }
}
