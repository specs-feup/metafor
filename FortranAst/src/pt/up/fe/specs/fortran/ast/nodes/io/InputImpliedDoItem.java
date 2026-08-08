package pt.up.fe.specs.fortran.ast.nodes.io;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

// TODO(Process-ing): Implement and use this node
public class InputImpliedDoItem extends InputItem {
    public InputImpliedDoItem(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
