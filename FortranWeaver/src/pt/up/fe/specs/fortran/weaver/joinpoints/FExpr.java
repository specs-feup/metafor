package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AExpr;

public class FExpr extends AExpr {

    private final Expr expr;

    public FExpr(Expr expr) {
        this.expr = expr;
    }

    @Override
    public FortranNode getNode() {
        return expr;
    }
}
