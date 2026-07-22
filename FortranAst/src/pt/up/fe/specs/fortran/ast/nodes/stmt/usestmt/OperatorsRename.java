package pt.up.fe.specs.fortran.ast.nodes.stmt.usestmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

// TODO(Process-ing): Complete and use this subclass
public class OperatorsRename extends Rename {
    public OperatorsRename(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
