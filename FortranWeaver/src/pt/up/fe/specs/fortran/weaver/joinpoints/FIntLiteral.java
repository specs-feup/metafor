package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.IntLiteral;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AIntLiteral;

public class FIntLiteral extends AIntLiteral {

    private final IntLiteral intLiteral;

    public FIntLiteral(IntLiteral intLiteral) {
        super(new FKindedLiteral(intLiteral));

        this.intLiteral = intLiteral;
    }

    @Override
    public FortranNode getNode() {
        return intLiteral;
    }
}
