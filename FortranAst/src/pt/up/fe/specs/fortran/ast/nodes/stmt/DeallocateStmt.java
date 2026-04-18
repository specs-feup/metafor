package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.DataRef;

import java.util.Collection;
import java.util.List;

public class DeallocateStmt extends ActionStmt {
    public DeallocateStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<DataRef> getRefs() {
        return getChildrenOf(DataRef.class);
    }
}
