package pt.up.fe.specs.fortran.ast.nodes.io;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public abstract class CloseSpec extends FortranNode {
    public CloseSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
