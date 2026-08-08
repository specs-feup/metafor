package pt.up.fe.specs.fortran.ast.nodes.specification.coshape;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;
import pt.up.fe.specs.fortran.ast.nodes.specification.shape.ExplicitShape;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExplicitCoshapeSpec extends CoarraySpec {
    public ExplicitCoshapeSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<ExplicitShape> getExplicitShapes() {
        return getChildrenOf(ExplicitShape.class);
    }

    public Optional<Expr> getLastLowerBound() {
        return getChildTry(Expr.class, getNumChildren() - 1);
    }

    @Override
    public String getCode() {
        var explicitShapesCode = getExplicitShapes().stream()
                .map(shape -> shape.getCode() + ", ")
                .collect(Collectors.joining());

        var lastLowerBoundCode = getLastLowerBound()
                .map(lowerBound -> lowerBound.getCode() + ":")
                .orElse("");

        return "[" + explicitShapesCode + lastLowerBoundCode + "*]";
    }
}
