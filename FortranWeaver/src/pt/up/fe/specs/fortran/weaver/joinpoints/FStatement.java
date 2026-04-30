package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AStatement;
import pt.up.fe.specs.util.exceptions.NotImplementedException;

public class FStatement extends AStatement {

    private final Stmt stmt;

    public FStatement(Stmt stmt) {
        this.stmt = stmt;
    }

    @Override
    public FortranNode getNode() {
        return stmt;
    }

    @Override
    public Boolean getIsFirstImpl() {
        throw new NotImplementedException(this);
    }

    @Override
    public Boolean getIsLastImpl() {
        throw new NotImplementedException(this);
    }


}
