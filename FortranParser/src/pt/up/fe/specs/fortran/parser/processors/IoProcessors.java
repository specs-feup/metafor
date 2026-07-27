package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.io.*;
import pt.up.fe.specs.fortran.ast.nodes.io.enums.*;
import pt.up.fe.specs.fortran.parser.FlangAttributes;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

import java.util.List;

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

    private String getCharExprKind(FlangAttributes attrs) {
        var kindId = attrs.getString(FlangName.KIND);
        return attributes().get(kindId).getString("value");
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

                var kind = ExprConnectSpecKind.convert(getCharExprKind(attrs));
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

    public void exprIoControlSpec(ExprIoControlSpec spec) {
        var attrs = attributes(spec);
        var nodeKind = getNodeKind(spec);

        if (nodeKind == FlangName.IO_UNIT) {  // If original node is IoUnit
            spec.set(ExprIoControlSpec.KIND, ExprIoControlSpecKind.UNIT);

        } else if (nodeKind == FlangName.IO_CONTROL_SPEC) {  // If original node is IoControlSpec
            var variant = attrs.getVariantName();
            attrs = getChildAttrs(attrs);

            var kind = switch (variant) {
                // ADVANCE, BLANK, DECIMAL, DELIM, PAD, ROUND, SIGN
                case CHAR_EXPR -> ExprIoControlSpecKind.convert(getCharExprKind(attrs));
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
        var nodeKind = getNodeKind(spec);

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
        var attrs = attributes(spec);
        var variant = attrs.getVariantName();

        var kind = switch (variant) {
            case END_LABEL -> LabelIoControlSpecKind.END;
            case EOR_LABEL -> LabelIoControlSpecKind.EOR;
            case ERR_LABEL -> LabelIoControlSpecKind.ERR;
            default -> throw new RuntimeException("Variant '" + variant + "' of LabelIoControlSpec is not handled.");
        };
        spec.set(LabelIoControlSpec.KIND, kind);

        var label = getChildAttrs(attrs).getString("uint64_t");
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

    public void rewindStmt(RewindStmt rewindStmt) {
        stmtProcessors.actionStmt(rewindStmt);

        if (attributes(rewindStmt).has(FlangName.POSITION_OR_FLUSH_SPEC)) {
            var children = getChildren(rewindStmt, FlangName.POSITION_OR_FLUSH_SPEC);
            rewindStmt.addChildren(children);
        }
    }

    public void unitPosFlushSpec(UnitPosFlushSpec spec) {
        var exprId = attributes().getString(spec, FlangName.EXPR.getString(), FlangName.FILE_UNIT_NUMBER);
        var expr = getChild(exprId);
        spec.addChild(expr);
    }

    public void varPosFlushSpec(VarPosFlushSpec spec) {
        var attrs = attributes(spec);
        var variant = attrs.getVariantName();

        var kind = switch (variant) {
            case MSG_VARIABLE -> VarPosFlushSpecKind.IOMSG;
            case STAT_VARIABLE -> VarPosFlushSpecKind.IOSTAT;
            default -> throw new RuntimeException("Variant '" + variant + "' of VarPosFlushSpec is not handled.");
        };
        spec.set(VarPosFlushSpec.KIND, kind);

        var variableId = getChildAttrs(attrs).getString(FlangName.VARIABLE);
        var variable = getChild(variableId);
        spec.addChild(variable);
    }

    public void errPosFlushSpec(ErrPosFlushSpec spec) {
        var label = attributes().getString(spec, "uint64_t", FlangName.ERR_LABEL);
        spec.set(ErrPosFlushSpec.LABEL, Integer.parseInt(label));
    }

    public void readStmt(ReadStmt readStmt) {
        stmtProcessors.actionStmt(readStmt);

        if (attributes(readStmt).has(FlangName.IO_UNIT)) {
            var ioUnit = getChild(readStmt, FlangName.IO_UNIT);
            readStmt.addChild(ioUnit);
        }

        if (attributes(readStmt).has(FlangName.FORMAT)) {
            var format = getChild(readStmt, FlangName.FORMAT);
            var spec = factory().newNode(FormatIoControlSpec.class, List.of(format));
            readStmt.addChild(spec);
        }

        if (attributes(readStmt).has(FlangName.IO_CONTROL_SPEC)) {
            var ioControlSpecs = getChildren(readStmt, FlangName.IO_CONTROL_SPEC);
            readStmt.addChildren(ioControlSpecs);
        }

        if (attributes(readStmt).has(FlangName.INPUT_ITEM)) {
            var inputItems = getChildren(readStmt, FlangName.INPUT_ITEM);
            readStmt.addChildren(inputItems);
        }
    }

    public void varInputItem(VarInputItem item) {
        var variable = getChild(item, FlangName.VARIABLE);
        item.addChild(variable);
    }

    public void writeStmt(WriteStmt writeStmt) {
        stmtProcessors.actionStmt(writeStmt);

        if (attributes(writeStmt).has(FlangName.IO_UNIT)) {
            writeStmt.addChild(getChild(writeStmt, FlangName.IO_UNIT));
        }

        if (attributes(writeStmt).has(FlangName.FORMAT)) {
            var format = getChild(writeStmt, FlangName.FORMAT);
            var spec = factory().newNode(FormatIoControlSpec.class, List.of(format));
            writeStmt.addChild(spec);
        }

        if (attributes(writeStmt).has(FlangName.IO_CONTROL_SPEC)) {
            writeStmt.addChildren(getChildren(writeStmt, FlangName.IO_CONTROL_SPEC));
        }

        if (attributes(writeStmt).has(FlangName.OUTPUT_ITEM)) {
            writeStmt.addChildren(getChildren(writeStmt, FlangName.OUTPUT_ITEM));
        }
    }

    public void exprOutputItem(ExprOutputItem item) {
        var expr = getChild(item, FlangName.EXPR);
        item.addChild(expr);
    }

    public void waitStmt(WaitStmt waitStmt) {
        stmtProcessors.actionStmt(waitStmt);

        var specs = getChildren(waitStmt, FlangName.WAIT_SPEC);
        waitStmt.addChildren(specs);
    }

    public void exprWaitSpec(ExprWaitSpec spec) {
        var variant = attributes(spec).getVariantName();

        var kind = switch (variant) {
            case FILE_UNIT_NUMBER -> ExprWaitSpecKind.UNIT;
            case ID_EXPR -> ExprWaitSpecKind.ID;
            default -> throw new RuntimeException("Variant '" + variant + "' of ExprWaitSpec is not handled.");
        };
        spec.set(ExprWaitSpec.KIND, kind);

        var exprId = attributes().getString(spec, FlangName.EXPR.getString(), variant);
        var expr = getChild(exprId);
        spec.addChild(expr);
    }

    public void varWaitSpec(VarWaitSpec spec) {
        var variant = attributes(spec).getVariantName();

        var kind = switch (variant) {
            case MSG_VARIABLE -> VarWaitSpecKind.IOMSG;
            case STAT_VARIABLE -> VarWaitSpecKind.IOSTAT;
            default -> throw new RuntimeException("Variant '" + variant + "' of VarWaitSpec is not handled.");
        };
        spec.set(VarWaitSpec.KIND, kind);

        var variableId = attributes().getString(spec, FlangName.VARIABLE.getString(), variant);
        var variable = getChild(variableId);
        spec.addChild(variable);
    }

    public void labelWaitSpec(LabelWaitSpec spec) {
        var variant = attributes(spec).getVariantName();

        var kind = switch (variant) {
            case END_LABEL -> LabelWaitSpecKind.END;
            case EOR_LABEL -> LabelWaitSpecKind.EOR;
            case ERR_LABEL -> LabelWaitSpecKind.ERR;
            default -> throw new RuntimeException("Variant '" + variant + "' of LabelWaitSpec is not handled.");
        };
        spec.set(LabelWaitSpec.KIND, kind);

        var label = attributes().getString(spec, "uint64_t", variant);
        spec.set(LabelWaitSpec.LABEL, Integer.parseInt(label));
    }

    public void closeStmt(CloseStmt closeStmt) {
        stmtProcessors.actionStmt(closeStmt);

        if (attributes(closeStmt).has(FlangName.CLOSE_SPEC)) {
            var closeSpecs = getChildren(closeStmt, FlangName.CLOSE_SPEC);
            closeStmt.addChildren(closeSpecs);
        }
    }

    public void exprCloseSpec(ExprCloseSpec spec) {
        var variant = attributes(spec).getVariantName();

        var kind = switch (variant) {
            case FILE_UNIT_NUMBER -> ExprCloseSpecKind.UNIT;
            case STATUS_EXPR -> ExprCloseSpecKind.STATUS;
            default -> throw new RuntimeException("Variant '" + variant + "' of ExprCloseSpec is not handled.");
        };
        spec.set(ExprCloseSpec.KIND, kind);

        var exprId = attributes().getString(spec, FlangName.EXPR.getString(), variant);
        var expr = getChild(exprId);
        spec.addChild(expr);
    }

    public void varCloseSpec(VarCloseSpec spec) {
        var variant = attributes(spec).getVariantName();

        var kind = switch (variant) {
            case STAT_VARIABLE -> VarCloseSpecKind.IOSTAT;
            case MSG_VARIABLE -> VarCloseSpecKind.IOMSG;
            default -> throw new RuntimeException("Variant '" + variant + "' of VarCloseSpec is not handled.");
        };
        spec.set(VarCloseSpec.KIND, kind);

        var variableId = attributes().getString(spec, FlangName.VARIABLE.getString(), variant);
        var variable = getChild(variableId);
        spec.addChild(variable);
    }

    public void errCloseSpec(ErrCloseSpec spec) {
        var label = attributes().getString(spec, "uint64_t", FlangName.ERR_LABEL);
        spec.set(ErrCloseSpec.LABEL, Integer.parseInt(label));
    }
}
