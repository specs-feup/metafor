package pt.up.fe.specs.fortran.ast.nodes.loops;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public abstract class LoopControl extends FortranNode {
    public LoopControl(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
