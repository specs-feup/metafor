package pt.up.fe.specs.fortran.ast.nodes.specification.shape;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ExplicitShapeArraySpec extends ArraySpec {
    public ExplicitShapeArraySpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<ExplicitShape> getExplicitShapes() {
        return getChildren(ExplicitShape.class);
    }

    @Override
    public String getCode() {
        return getExplicitShapes().stream()
                .map(ExplicitShape::getCode)
                .collect(Collectors.joining(", ", "(", ")"));
    }
}
