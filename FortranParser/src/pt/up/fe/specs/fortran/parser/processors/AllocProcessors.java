package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.alloc.Allocation;
import pt.up.fe.specs.fortran.ast.nodes.alloc.ExprAllocOption;
import pt.up.fe.specs.fortran.ast.nodes.alloc.VarAllocOption;
import pt.up.fe.specs.fortran.ast.nodes.alloc.enums.ExprAllocOptionKind;
import pt.up.fe.specs.fortran.ast.nodes.alloc.enums.VarAllocOptionKind;
import pt.up.fe.specs.fortran.parser.FlangAttributes;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

public class AllocProcessors extends ANodeProcessor {
    public AllocProcessors(FortranJsonResult data) {
        super(data);
    }

    public void allocation(Allocation allocation) {
        allocation.addChild(getChild(allocation, FlangName.ALLOCATE_OBJECT));
        allocation.addChildren(getChildren(allocation, FlangName.ALLOCATE_SHAPE_SPEC));
    }

    public void exprAllocOption(ExprAllocOption option) {
        var attrs = attributes(option);
        var variant = attrs.getVariantName();

        var kind = switch (variant) {
            case MOLD -> ExprAllocOptionKind.MOLD;
            case SOURCE -> ExprAllocOptionKind.SOURCE;
            case STREAM -> ExprAllocOptionKind.STREAM;
            default -> throw new RuntimeException("Unknown variant: " + variant);
        };
        option.set(ExprAllocOption.KIND, kind);

        var exprId = attributes().get(variant.toString()).getString(FlangName.EXPR);
        var expr = getChild(exprId);
        option.addChild(expr);
    }

    public void varAllocOption(VarAllocOption option) {
        var attrs = attributes(option);
        var variant = attrs.getVariantName();

        if (variant == FlangName.STAT_OR_ERRMSG) {
            attrs = attributes().get(variant.toString());
            variant = attrs.getVariantName();
        }

        var kind = switch (variant) {
            case STAT_VARIABLE -> VarAllocOptionKind.STAT;
            case MSG_VARIABLE -> VarAllocOptionKind.ERRMSG;
            case PINNED -> VarAllocOptionKind.PINNED;
            default -> throw new RuntimeException("Unknown variant: " + variant);
        };
        option.set(VarAllocOption.KIND, kind);

        var variableId = attributes().get(variant.toString()).getString(FlangName.VARIABLE);
        var variable = getChild(variableId);
        option.addChild(variable);
    }
}
