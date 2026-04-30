package pt.up.fe.specs.fortran.ast.nodes.type.shapes;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class AllocateShapeSpecification extends BoundedShapeSpecification {
    public AllocateShapeSpecification(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
