package pt.up.fe.specs.fortran.ast.nodes.type.lenselector;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public abstract class LenSelector extends FortranNode {
    public LenSelector(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
