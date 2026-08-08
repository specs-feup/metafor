package pt.up.fe.specs.fortran.ast.nodes.specification.shape;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ImpliedShapeSpec extends ArraySpec {
    public ImpliedShapeSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<AssumedImpliedShape> getShapes() {
        return getChildren(AssumedImpliedShape.class);
    }

    @Override
    public String getCode() {
        return getShapes().stream()
                .map(AssumedImpliedShape::getCode)
                .collect(Collectors.joining(", "));
    }
}
