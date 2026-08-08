package pt.up.fe.specs.fortran.ast.nodes.stmt.usestmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

// TODO(Process-ing): Complete and use this subclass
public class OnlyGenericSpec extends Only {
    public OnlyGenericSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
