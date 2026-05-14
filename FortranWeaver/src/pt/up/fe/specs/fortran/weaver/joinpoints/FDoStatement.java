package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.DoStmt;
import pt.up.fe.specs.fortran.weaver.FortranJoinpoints;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.ADoStatement;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AExecution;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.ALoopControl;

public class FDoStatement extends ADoStatement {

    private final DoStmt doStmt;

    public FDoStatement(DoStmt doStmt) {
        super(new FExecutableStatement(doStmt));
        this.doStmt = doStmt;
    }

    @Override
    public AExecution getBodyImpl() {
        return FortranJoinpoints.create(doStmt.getBody(), AExecution.class);
    }

    @Override
    public ALoopControl getControlImpl() {
        return FortranJoinpoints.create(doStmt.getControl().get(), ALoopControl.class);
    }

    @Override
    public ADoStatement copyScopeImpl() {
        DoStmt copiedDoStmt = (DoStmt) doStmt.copy();
        copiedDoStmt.getBody().removeChildren();
        return new FDoStatement(copiedDoStmt);
    }

    @Override
    public FortranNode getNode() {
        return doStmt;
    }
}
