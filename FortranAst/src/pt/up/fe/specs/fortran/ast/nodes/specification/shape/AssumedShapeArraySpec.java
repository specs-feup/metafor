package pt.up.fe.specs.fortran.ast.nodes.specification.shape;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AssumedShapeArraySpec extends ArraySpec {
    public AssumedShapeArraySpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<AssumedShape> getAssumedShapes() {
        return getChildren(AssumedShape.class);
    }

    @Override
    public String getCode() {
        return getAssumedShapes().stream()
                .map(AssumedShape::getCode)
                .collect(Collectors.joining(", ", "(", ")"));
    }
}
