package pt.up.fe.specs.fortran.ast.nodes.alloc;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public abstract class AllocOption extends FortranNode {
    public AllocOption(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
