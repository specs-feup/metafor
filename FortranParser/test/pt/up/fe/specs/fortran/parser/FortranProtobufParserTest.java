package pt.up.fe.specs.fortran.parser;

import flang_dumper.protocol.AstStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pt.up.fe.specs.fortran.ast.FortranContext;
import pt.up.fe.specs.util.SpecsSystem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FortranProtobufParserTest {

    private static final long ROOT_ID = 0x8000_0000_0000_0001L;
    private static final long CHILD_ID = 2L;
    private static final String ROOT_KEY = "0x8000000000000001-MainProgram";
    private static final String CHILD_KEY = "0x2-Name";

    @BeforeAll
    static void setupOnce() {
        SpecsSystem.programStandardInit();
    }

    @Test
    void parsesGraphValuesAndComments() throws IOException {
        AstStream.ValueList nestedValues = AstStream.ValueList.newBuilder()
                .addItems(AstStream.Value.newBuilder().setBoolValue(true))
                .addItems(AstStream.Value.newBuilder().setNodeReference(CHILD_ID))
                .build();
        AstStream.ValueList list = AstStream.ValueList.newBuilder()
                .addItems(AstStream.Value.newBuilder().setStringValue("first"))
                .addItems(AstStream.Value.newBuilder().setIntegerValue(-7))
                .addItems(AstStream.Value.newBuilder().setListValue(nestedValues))
                .addItems(AstStream.Value.newBuilder().setNullValue(AstStream.NullValue.getDefaultInstance()))
                .build();

        AstStream.NodeRecord root = node(ROOT_ID, "MainProgram")
                .addAttributes(attribute("forward", AstStream.AttributeValue.newBuilder()
                        .setNodeReference(CHILD_ID).build()))
                .addAttributes(attribute("unsigned", AstStream.AttributeValue.newBuilder()
                        .setUint64Value(-1L).build()))
                .addAttributes(attribute("signed", AstStream.AttributeValue.newBuilder()
                        .setIntegerValue(-7).build()))
                .addAttributes(attribute("nested", AstStream.AttributeValue.newBuilder()
                        .setListValue(list).build()))
                .addAttributes(attribute("duplicate", AstStream.AttributeValue.newBuilder()
                        .setStringValue("old").build()))
                .addAttributes(attribute("duplicate", AstStream.AttributeValue.newBuilder()
                        .setStringValue("new").build()))
                .build();

        AstStream.EnumCatalog catalog = AstStream.EnumCatalog.newBuilder()
                .setEnumTypeName("ExampleEnum")
                .addEntries(AstStream.EnumEntry.newBuilder().setName("A").setNumber(1))
                .addEntries(AstStream.EnumEntry.newBuilder().setName("B").setNumber(2))
                .build();

        AstStream.StreamRecord commentForForwardNode = AstStream.StreamRecord.newBuilder()
                .setComment(AstStream.CommentRecord.newBuilder()
                        .setStmtNodeId(CHILD_ID).setText("child lead"))
                .build();
        AstStream.StreamRecord firstTrailingComment = comment(ROOT_ID, "old trailing", true);
        AstStream.StreamRecord child = AstStream.StreamRecord.newBuilder()
                .setNode(node(CHILD_ID, "Name").build())
                .build();

        FortranJsonResult result = parse(stream(
                AstStream.StreamRecord.newBuilder().setNode(root).build(),
                commentForForwardNode,
                firstTrailingComment,
                AstStream.StreamRecord.newBuilder().setEnumCatalog(catalog).build(),
                child,
                comment(ROOT_ID, "root lead 1", false),
                comment(ROOT_ID, "root lead 2", false),
                comment(ROOT_ID, "new trailing", true)));

        assertEquals(ROOT_KEY, result.firstNode());
        assertEquals(List.of(ROOT_KEY, CHILD_KEY), List.copyOf(result.ids()));
        assertTrue(result.fortranNodes().containsKey(ROOT_KEY));
        assertEquals(CHILD_KEY, result.attributes().getAttrs(ROOT_KEY).getString("forward"));
        assertEquals("18446744073709551615", result.attributes().getAttrs(ROOT_KEY).getString("unsigned"));
        assertEquals("-7", result.attributes().getAttrs(ROOT_KEY).getString("signed"));
        assertEquals("new", result.attributes().getAttrs(ROOT_KEY).getString("duplicate"));

        Object nestedObject = result.attributes().getAttrs(ROOT_KEY).getOptional("nested").orElseThrow();
        List<?> nestedList = assertInstanceOf(List.class, nestedObject);
        assertEquals("first", nestedList.get(0));
        assertEquals("-7", nestedList.get(1));
        List<?> innerList = assertInstanceOf(List.class, nestedList.get(2));
        assertEquals("1", innerList.get(0));
        assertEquals(CHILD_KEY, innerList.get(1));
        assertTrue(nestedList.contains(null));

        assertEquals(List.of("child lead"), result.attributes().getAttrs(CHILD_KEY)
                .getList("leadingComments", Object::toString));
        assertEquals(List.of("root lead 1", "root lead 2"), result.attributes().getAttrs(ROOT_KEY)
                .getList("leadingComments", Object::toString));
        assertEquals("new trailing", result.attributes().getAttrs(ROOT_KEY).getString("trailingComment"));
    }

    @Test
    void rejectsTruncatedFrameInvalidVersionAndNoncanonicalLength() throws IOException {
        byte[] truncatedPayload = header();
        ByteArrayOutputStream truncated = new ByteArrayOutputStream();
        truncated.write(truncatedPayload);
        truncated.write(2);
        truncated.write(0x0a);
        assertParseFailure(truncated.toByteArray(), "truncated protobuf record payload");

        byte[] badVersion = header();
        badVersion[8] = 2;
        assertParseFailure(badVersion, "unsupported protocol version");

        ByteArrayOutputStream noncanonical = new ByteArrayOutputStream();
        noncanonical.write(header());
        noncanonical.write(0x81);
        noncanonical.write(0x00);
        assertParseFailure(noncanonical.toByteArray(), "record length varint is not canonical");
    }

    @Test
    void rejectsZeroAndDuplicateNodeIdsAndUnsetAttributeValues() throws IOException {
        AstStream.StreamRecord zeroId = AstStream.StreamRecord.newBuilder()
                .setNode(node(0, "MainProgram").build())
                .build();
        assertParseFailure(stream(zeroId), "node ID must be nonzero");

        AstStream.StreamRecord repeated = AstStream.StreamRecord.newBuilder()
                .setNode(node(9, "MainProgram").build())
                .build();
        assertParseFailure(stream(repeated, repeated), "duplicate node ID 9");

        AstStream.NodeRecord missingValue = node(9, "MainProgram")
                .addAttributes(AstStream.Attribute.newBuilder().setKey("missing").build())
                .build();
        assertParseFailure(stream(AstStream.StreamRecord.newBuilder().setNode(missingValue).build()),
                "value oneof must be set");
    }

    private static AstStream.NodeRecord.Builder node(long id, String kindName) {
        return AstStream.NodeRecord.newBuilder()
                .setNodeId(id)
                .setKindId(1)
                .setKindName(kindName);
    }

    private static AstStream.Attribute attribute(String key, AstStream.AttributeValue value) {
        return AstStream.Attribute.newBuilder().setKey(key).setValue(value).build();
    }

    private static AstStream.StreamRecord comment(long nodeId, String text, boolean trailing) {
        return AstStream.StreamRecord.newBuilder()
                .setComment(AstStream.CommentRecord.newBuilder()
                        .setStmtNodeId(nodeId)
                        .setText(text)
                        .setTrailing(trailing))
                .build();
    }

    private static FortranJsonResult parse(byte[] bytes) {
        return FortranProtobufParser.parse(new ByteArrayInputStream(bytes), new FortranContext());
    }

    private static void assertParseFailure(byte[] bytes, String causeMessage) {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> parse(bytes));
        assertEquals("Problem while parsing Fortran protobuf", exception.getMessage());
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause().getMessage().contains(causeMessage),
                () -> "Expected cause containing '" + causeMessage + "', got '"
                        + exception.getCause().getMessage() + "'");
    }

    private static byte[] stream(AstStream.StreamRecord... records) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(header());
        for (AstStream.StreamRecord record : records) {
            byte[] payload = record.toByteArray();
            writeVarint(output, payload.length);
            output.write(payload);
        }
        return output.toByteArray();
    }

    private static byte[] header() {
        return new byte[]{'F', 'L', 'A', 'S', 'T', 'P', 'B', '1', 1, 0, 0, 0};
    }

    private static void writeVarint(ByteArrayOutputStream output, int value) {
        while ((value & ~0x7f) != 0) {
            output.write((value & 0x7f) | 0x80);
            value >>>= 7;
        }
        output.write(value);
    }
}
