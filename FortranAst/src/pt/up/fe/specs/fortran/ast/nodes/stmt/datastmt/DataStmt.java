package pt.up.fe.specs.fortran.ast.nodes.stmt.datastmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.DeclarationStmt;

import java.util.Collection;
import java.util.List;

public class DataStmt extends DeclarationStmt {
    public DataStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<DataStmtSet> getDataSets() {
        return getChildren(DataStmtSet.class);
    }
}
