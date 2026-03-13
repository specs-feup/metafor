package pt.up.fe.specs.fortran.ast.nodes.omp;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.DoStmt;

import java.util.Collection;

public class OmpLoopConstruct extends OmpConstruct {
    public OmpLoopConstruct(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public DoStmt getLoop() {
        return getChild(DoStmt.class);
    }
}
