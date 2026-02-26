package pt.up.fe.specs.fortran.ast.nodes.type.shapes;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public abstract class ShapeSpecification extends FortranNode {
    public ShapeSpecification(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
