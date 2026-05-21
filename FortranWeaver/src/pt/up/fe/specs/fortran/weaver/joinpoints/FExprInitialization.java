package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.ExprInitialization;
import pt.up.fe.specs.fortran.weaver.FortranJoinpoints;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AExpr;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AExprInitialization;

public class FExprInitialization extends AExprInitialization {

    public final ExprInitialization exprInitialization;

    public FExprInitialization(ExprInitialization exprInitialization) {
        super(new FInitialization(exprInitialization));
        this.exprInitialization = exprInitialization;
    }

    @Override
    public AExpr getExprImpl() {
        return FortranJoinpoints.create(exprInitialization.getExpr(), AExpr.class);
    }

    @Override
    public FortranNode getNode() {
        return exprInitialization;
    }
}
