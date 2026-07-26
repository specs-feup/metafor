package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.io.*;
import pt.up.fe.specs.fortran.ast.nodes.io.enums.*;
import pt.up.fe.specs.fortran.parser.FlangAttributes;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

public class IoProcessors extends ANodeProcessor {
    private final StmtProcessors stmtProcessors;

    public IoProcessors(FortranJsonResult data, StmtProcessors stmtProcessors) {
        super(data);

        this.stmtProcessors = stmtProcessors;
    }

    public void openStmt(OpenStmt openStmt) {
        stmtProcessors.actionStmt(openStmt);

        var specs = getChildren(openStmt, FlangName.CONNECT_SPEC);
        openStmt.addChildren(specs);
    }

    public void exprConnectSpec(ExprConnectSpec spec) {
        var attrs = attributes(spec);
        var variant = attrs.getVariantName();

        switch (variant) {
            case FILE_UNIT_NUMBER -> {  // UNIT
                attrs = getChildAttrs(attrs);
                spec.set(ExprConnectSpec.KIND, ExprConnectSpecKind.UNIT);
            }

            case EXPR -> {  // FILE
                spec.set(ExprConnectSpec.KIND, ExprConnectSpecKind.FILE);
            }

            // ACCESS, ACTION, ASYNCHRONOUS, BLANK, DECIMAL, DELIM, ENCODING, FORM, PAD, POSITION, ROUND, SIGN,
            // CARRIAGECONTROL, CONVERT, DISPOSE
            case CHAR_EXPR -> {
                attrs = getChildAttrs(attrs);

                var flangKind = attrs.getString(FlangName.KIND);
                var kind = ExprConnectSpecKind.valueOf(flangKind);
                spec.set(ExprConnectSpec.KIND, kind);
            }

            case RECL -> {  // RECL
                attrs = getChildAttrs(attrs);
                spec.set(ExprConnectSpec.KIND, ExprConnectSpecKind.RECL);
            }

            case STATUS_EXPR -> {  // STATUS
                attrs = getChildAttrs(attrs);
                spec.set(ExprConnectSpec.KIND, ExprConnectSpecKind.STATUS);
            }

            default -> {
                throw new RuntimeException("Variant '" + variant + "' of ExprConnectSpec is not handled.");
            }
        }

        var exprId = attrs.getString(FlangName.EXPR);
        var expr = getChild(exprId);
        spec.addChild(expr);
    }

    private FlangAttributes getChildAttrs(FlangAttributes attrs) {
        return attributes().get(attrs.getVariantString());
    }

    public void varConnectSpec(VarConnectSpec spec) {
        var attrs = attributes(spec);
        var variant = attrs.getVariantName();

        var kind = switch (variant) {
            case MSG_VARIABLE -> VarConnectSpecKind.IOMSG;
            case STAT_VARIABLE -> VarConnectSpecKind.IOSTAT;
            case NEWUNIT -> VarConnectSpecKind.NEWUNIT;
            default -> throw new RuntimeException("Variant '" + variant + "' of VarConnectSpec is not handled.");
        };
        spec.set(VarConnectSpec.KIND, kind);

        var childAttrs = attributes().get(attrs.getVariantString());
        var variable = getChild(childAttrs.getString(FlangName.VARIABLE));
        spec.addChild(variable);
    }

    public void errConnectSpec(ErrConnectSpec spec) {
        var label = attributes(spec).getString("uint64_t");
        spec.set(ErrConnectSpec.LABEL, Integer.parseInt(label));
    }


    public void writeStmt(WriteStmt writeStmt) {
        stmtProcessors.actionStmt(writeStmt);

        if (attributes(writeStmt).has("iounit")) {
            writeStmt.addChild(getChild(writeStmt, "iounit"));
        }

        if (attributes(writeStmt).has("format")) {
            writeStmt.addChild(getChild(writeStmt, "format"));
        }

        if (attributes(writeStmt).has("controls")) {
            writeStmt.addChildren(getChildren(writeStmt, "controls"));
        }

        if (attributes(writeStmt).has("items")) {
            writeStmt.addChildren(getChildren(writeStmt, "items"));
        }
    }

    public void exprIoControlSpec(ExprIoControlSpec spec) {
        var attrs = attributes(spec);
        var nodeKind = getKind(spec);

        if (nodeKind == FlangName.IO_UNIT) {  // If original node is IoUnit
            spec.set(ExprIoControlSpec.KIND, ExprIoControlSpecKind.UNIT);

        } else if (nodeKind == FlangName.IO_CONTROL_SPEC) {  // If original node is IoControlSpec
            var variant = attrs.getVariantName();
            attrs = getChildAttrs(attrs);

            var kind = switch (variant) {
                // ADVANCE, BLANK, DECIMAL, DELIM, PAD, ROUND, SIGN
                case CHAR_EXPR -> ExprIoControlSpecKind.valueOf(attrs.getString(FlangName.KIND));
                case ASYNCHRONOUS -> ExprIoControlSpecKind.ASYNCHRONOUS;
                case POS -> ExprIoControlSpecKind.POS;
                case REC -> ExprIoControlSpecKind.REC;
                default -> throw new RuntimeException("Variant '" + variant + "' of ExprIoControlSpec is not handled.");
            };
            spec.set(ExprIoControlSpec.KIND, kind);

        } else {
            throw new RuntimeException("Node kind '" + nodeKind + "' of ExprIoControlSpec is not handled.");
        }

        var exprId = attrs.getString(FlangName.EXPR);
        var expr = getChild(exprId);
        spec.addChild(expr);
    }

    public void varIoControlSpec(VarIoControlSpec spec) {
        var attrs = attributes(spec);
        var nodeKind = getKind(spec);

        if (nodeKind == FlangName.IO_UNIT) {
            spec.set(VarIoControlSpec.KIND, VarIoControlSpecKind.UNIT);

        } else if (nodeKind == FlangName.IO_CONTROL_SPEC) {
            var variant = attrs.getVariantName();
            attrs = getChildAttrs(attrs);

            var kind = switch (variant) {
                case ID_VARIABLE -> VarIoControlSpecKind.ID;
                case MSG_VARIABLE -> VarIoControlSpecKind.IOMSG;
                case STAT_VARIABLE -> VarIoControlSpecKind.IOSTAT;
                case SIZE -> VarIoControlSpecKind.SIZE;
                default -> throw new RuntimeException("Variant '" + variant + "' of VarIoControlSpec is not handled.");
            };
            spec.set(VarIoControlSpec.KIND, kind);

        } else {
            throw new RuntimeException("Node kind '" + nodeKind + "' of VarIoControlSpec is not handled.");
        }

        var variableId = attrs.getString(FlangName.VARIABLE);
        var variable = getChild(variableId);
        spec.addChild(variable);
    }

    public void labelIoControlSpec(LabelIoControlSpec spec) {
        var variant = attributes(spec).getVariantName();

        var kind = switch (variant) {
            case END_LABEL -> LabelIoControlSpecKind.END;
            case EOR_LABEL -> LabelIoControlSpecKind.EOR;
            case ERR_LABEL -> LabelIoControlSpecKind.ERR;
            default -> throw new RuntimeException("Variant '" + variant + "' of LabelIoControlSpec is not handled.");
        };
        spec.set(LabelIoControlSpec.KIND, kind);

        var label = attributes(spec).getString("uint64_t");
        spec.set(LabelIoControlSpec.LABEL, Integer.parseInt(label));
    }

    public void starUnitIoControlSpec(StarUnitIoControlSpec ignoredSpec) {}

    public void formatIoControlSpec(FormatIoControlSpec spec) {
        var format = getChild(spec, FlangName.FORMAT);
        spec.addChild(format);
    }

    public void namelistIoControlSpec(NamelistIoControlSpec spec) {
        var namelistName = attributes().getString(spec, "source", FlangName.NAME);
        spec.set(NamelistIoControlSpec.NAMELIST_NAME, namelistName);
    }
}
