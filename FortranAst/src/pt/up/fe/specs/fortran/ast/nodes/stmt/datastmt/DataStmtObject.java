package pt.up.fe.specs.fortran.ast.nodes.stmt.datastmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

// TODO(Process-ing): Support rule data-stmt-object -> data-implied-do
public abstract class DataStmtObject extends FortranNode {
    public DataStmtObject(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
