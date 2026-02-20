package pt.up.fe.specs.fortran.ast.nodes.loops;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class ALoopControl extends FortranNode {
    public ALoopControl(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
