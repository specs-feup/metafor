package pt.up.fe.specs.fortran.ast.nodes.specification.genericspec;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public abstract class GenericSpec extends FortranNode {
    public GenericSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
