package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Literal;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.ALiteral;

public class FLiteral extends ALiteral {

    private final Literal literal;

    public FLiteral(Literal literal) {
        super(new FExpr(literal));

        this.literal = literal;
    }

    @Override
    public FortranNode getNode() {
        return literal;
    }
}
