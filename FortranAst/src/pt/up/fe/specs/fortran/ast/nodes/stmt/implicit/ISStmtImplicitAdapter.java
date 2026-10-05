package pt.up.fe.specs.fortran.ast.nodes.stmt.implicit;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ISStmt;

import java.util.Collection;

public class ISStmtImplicitAdapter extends ImplicitPartStmt {
    public ISStmtImplicitAdapter(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public ISStmt getStmt() {
        return getChild(ISStmt.class, 0);
    }

    @Override
    public String getStmtCode() {
        return getStmt().getCode();
    }
}
