package pt.up.fe.specs.fortran.ast.nodes.program.subprogram;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class InternalSubprogram extends Subprogram {
    public InternalSubprogram(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
