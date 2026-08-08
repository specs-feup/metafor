package pt.up.fe.specs.fortran.ast.nodes.type.decltype;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public abstract class DeclType extends FortranNode {
    public DeclType(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
