package pt.up.fe.specs.fortran.ast.nodes.alloc;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.dataref.AllocObject;
import pt.up.fe.specs.fortran.ast.nodes.specification.shape.ExplicitShape;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Allocation extends FortranNode {
    public Allocation(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public AllocObject getObject() {
        return getChild(AllocObject.class);
    }

    public List<ExplicitShape> getShapes() {
        return getChildrenOf(ExplicitShape.class);
    }

    public List<AllocOption> getOptions() {
        return getChildrenOf(AllocOption.class);
    }

    @Override
    public String getCode() {
        var objectCode = getObject().getCode();

        var shapes = getShapes();
        var shapesCode = shapes.isEmpty() ? ""
                : shapes.stream()
                        .map(ExplicitShape::getCode)
                        .collect(Collectors.joining(", ", "(", ")"));

        return objectCode + shapesCode;
    }
}
