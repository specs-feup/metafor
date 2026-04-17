package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.AssignmentStmt;
import pt.up.fe.specs.fortran.weaver.FortranJoinpoints;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AAssignmentStatement;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.ADataRef;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AExpr;

public class FAssignmentStatement extends AAssignmentStatement {

    private final AssignmentStmt assignmentStmt;

    public FAssignmentStatement(AssignmentStmt assignmentStmt) {
        super(new FActionStatement(assignmentStmt));
        this.assignmentStmt = assignmentStmt;
    }

    @Override
    public AExpr getExprImpl() {
        return FortranJoinpoints.create(assignmentStmt.getExpression(), AExpr.class);
    }

    @Override
    public ADataRef getVariableImpl() {
        return FortranJoinpoints.create(assignmentStmt.getVariable(), ADataRef.class);
    }

    @Override
    public FortranNode getNode() {
        return assignmentStmt;
    }
}
