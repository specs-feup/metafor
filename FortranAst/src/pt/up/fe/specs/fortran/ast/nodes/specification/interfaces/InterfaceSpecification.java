package pt.up.fe.specs.fortran.ast.nodes.specification.interfaces;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public abstract class InterfaceSpecification extends FortranNode {
    public InterfaceSpecification(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
