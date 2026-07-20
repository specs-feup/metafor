package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.loop.DoConstruct;
import pt.up.fe.specs.fortran.weaver.FortranJoinpoints;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.ADoConstruct;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AExecution;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.ALoopControl;

import java.util.Objects;

public class FDoConstruct extends ADoConstruct {

    private final DoConstruct doConstruct;

    public FDoConstruct(DoConstruct doConstruct) {
        this.doConstruct = doConstruct;
    }

    @Override
    public AExecution getBodyImpl() {
        return FortranJoinpoints.create(doConstruct.getBody(), AExecution.class);
    }

    @Override
    public ALoopControl getControlImpl() {
        return FortranJoinpoints.create(doConstruct.getControl().get(), ALoopControl.class);
    }

    @Override
    public String getKindImpl() {
        return doConstruct.getKind().toString().toLowerCase();
    }

    @Override
    public ADoConstruct copyScopeImpl() {
        DoConstruct copiedDoStmt = (DoConstruct) doConstruct.copy();
        copiedDoStmt.getBody().removeChildren();
        return new FDoConstruct(copiedDoStmt);
    }

    @Override
    public boolean sameScopeImpl(ADoConstruct loop) {
        if (!Objects.equals(this.getKindImpl(), loop.getKindImpl())) {
            return false;
        }
        return Objects.equals(this.getControlImpl().getCodeImpl(), loop.getControlImpl().getCodeImpl());
    }

    @Override
    public FortranNode getNode() {
        return doConstruct;
    }
}
