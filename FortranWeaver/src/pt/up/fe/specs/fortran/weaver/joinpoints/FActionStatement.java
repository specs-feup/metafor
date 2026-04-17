package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ActionStmt;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AActionStatement;

public class FActionStatement extends AActionStatement {

    private final ActionStmt actionStmt;

    public FActionStatement(ActionStmt actionStmt) {
        super(new FExecutableStatement(actionStmt));
        this.actionStmt = actionStmt;
    }

    @Override
    public FortranNode getNode() {
        return actionStmt;
    }
}
