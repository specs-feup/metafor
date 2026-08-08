package pt.up.fe.specs.fortran.ast.nodes.specification.shape;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AssumedSizeSpec extends ArraySpec {
    public AssumedSizeSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<ExplicitShape> getExplicitShapes() {
        return getChildrenOf(ExplicitShape.class);
    }

    public AssumedImpliedShape getAssumedImpliedShape() {
        return getChild(AssumedImpliedShape.class, getNumChildren() - 1);
    }

    @Override
    public String getCode() {
        var explicitShapesCode = getExplicitShapes().stream()
                .map(ExplicitShape::getCode)
                .collect(Collectors.joining(", "));

        var assumedImpliedShapeCode = getAssumedImpliedShape().getCode();

        return explicitShapesCode + ", " + assumedImpliedShapeCode;
    }
}
