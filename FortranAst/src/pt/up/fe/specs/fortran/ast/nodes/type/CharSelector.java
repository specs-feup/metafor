package pt.up.fe.specs.fortran.ast.nodes.type;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class CharSelector extends FortranNode {
    public CharSelector(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
