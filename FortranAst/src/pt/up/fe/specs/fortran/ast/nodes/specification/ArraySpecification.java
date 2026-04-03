package pt.up.fe.specs.fortran.ast.nodes.specification;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.type.shapes.ShapeSpecification;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ArraySpecification extends FortranNode {
    public ArraySpecification(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    private List<ShapeSpecification> getShapes() {
        return getChildren(ShapeSpecification.class);
    }

    @Override
    public String getCode() {
        var shapes = getShapes();
        var code = new StringBuilder();

        code.append("(");
        var shapesCode = shapes.stream().map(ShapeSpecification::getCode).collect(Collectors.joining(", "));
        code.append(shapesCode);
        code.append(")");
        return code.toString();
    }
}
