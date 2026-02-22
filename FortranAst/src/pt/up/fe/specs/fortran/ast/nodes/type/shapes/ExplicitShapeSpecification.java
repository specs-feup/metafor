package pt.up.fe.specs.fortran.ast.nodes.type.shapes;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.IntLiteral;

import java.util.Collection;
import java.util.stream.Collectors;

public class ExplicitShapeSpecification extends ShapeSpecification {
    public ExplicitShapeSpecification(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var bounds = getChildren(IntLiteral.class);
        return bounds.stream().map(IntLiteral::getCode).collect(Collectors.joining(":"));
    }
}
