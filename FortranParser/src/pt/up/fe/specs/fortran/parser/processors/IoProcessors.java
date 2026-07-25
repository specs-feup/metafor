package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.io.ErrConnectSpec;
import pt.up.fe.specs.fortran.ast.nodes.io.ExprConnectSpec;
import pt.up.fe.specs.fortran.ast.nodes.io.OpenStmt;
import pt.up.fe.specs.fortran.ast.nodes.io.VarConnectSpec;
import pt.up.fe.specs.fortran.ast.nodes.io.enums.ExprConnectSpecKind;
import pt.up.fe.specs.fortran.ast.nodes.io.enums.VarConnectSpecKind;
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
        var variantKey = attrs.getVariantKey();
        var variant = FlangName.convertTry(variantKey).orElseThrow(() -> new RuntimeException("Variant key '"
                + variantKey + "' of ExprConnectSpec is not a valid Flang name."));

        switch (variant) {
            case FILE_UNIT_NUMBER -> {  // UNIT
                attrs = attributes().get(attrs.getVariantString());
                spec.set(ExprConnectSpec.KIND, ExprConnectSpecKind.UNIT);
            }

            case EXPR -> {  // FILE
                spec.set(ExprConnectSpec.KIND, ExprConnectSpecKind.FILE);
            }

            // ACCESS, ACTION, ASYNCHRONOUS, BLANK, DECIMAL, DELIM, ENCODING, FORM, PAD, POSITION, ROUND, SIGN,
            // CARRIAGECONTROL, CONVERT, DISPOSE
            case CHAR_EXPR -> {
                attrs = attributes().get(attrs.getVariantString());

                var flangKind = attrs.getString(FlangName.KIND);
                var kind = ExprConnectSpecKind.valueOf(flangKind);
                spec.set(ExprConnectSpec.KIND, kind);
            }

            case RECL -> {  // RECL
                attrs = attributes().get(attrs.getVariantString());
                spec.set(ExprConnectSpec.KIND, ExprConnectSpecKind.RECL);
            }

            case STATUS_EXPR -> {  // STATUS
                attrs = attributes().get(attrs.getVariantString());
                spec.set(ExprConnectSpec.KIND, ExprConnectSpecKind.STATUS);
            }

            default -> {
                throw new RuntimeException("Variant key '" + variantKey + "' of ExprConnectSpec is not handled.");
            }
        }

        var exprId = attrs.getString(FlangName.EXPR);
        var expr = getChild(exprId);
        spec.addChild(expr);
    }

    public void varConnectSpec(VarConnectSpec spec) {
        var attrs = attributes(spec);
        var variantKey = attrs.getVariantKey();
        var variant = FlangName.convertTry(variantKey).orElseThrow(() -> new RuntimeException("Variant key '"
                + variantKey + "' of VarConnectSpec is not a valid Flang name."));

        var kind = switch (variant) {
            case MSG_VARIABLE -> VarConnectSpecKind.IOMSG;
            case STAT_VARIABLE -> VarConnectSpecKind.IOSTAT;
            case NEWUNIT -> VarConnectSpecKind.NEWUNIT;
            default -> throw new RuntimeException("Variant key '" + variantKey + "' of VarConnectSpec is not handled.");
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
}
