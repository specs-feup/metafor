package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.KindedLiteral;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AKindedLiteral;

public class FKindedLiteral extends AKindedLiteral {
    private final KindedLiteral kindedLiteral;

    public FKindedLiteral(KindedLiteral kindedLiteral) {
        super(new FLiteral(kindedLiteral));
        this.kindedLiteral = kindedLiteral;
    }

    @Override
    public FortranNode getNode() {
        return kindedLiteral;
    }
}
