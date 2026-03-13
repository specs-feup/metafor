package pt.up.fe.specs.fortran.ast.nodes.omp;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;

import java.util.Collection;

public class OmpBlockConstruct extends OmpConstruct {
    public OmpBlockConstruct(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Execution getBody() {
        return getChild(Execution.class);
    }
}
