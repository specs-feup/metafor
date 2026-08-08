package pt.up.fe.specs.fortran.ast.nodes.specification.type;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public abstract class TypeAttr extends FortranNode {
    public TypeAttr(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
