package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class CloseStmt extends ActionStmt {
    public CloseStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    //TODO properly support the statement
    @Override
    public String getStmtCode() {
        return get(SOURCE).replaceFirst("^\\d+", "");
    }
}
