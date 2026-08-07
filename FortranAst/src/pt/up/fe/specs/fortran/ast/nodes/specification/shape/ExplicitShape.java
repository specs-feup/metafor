package pt.up.fe.specs.fortran.ast.nodes.specification.shape;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;

import java.util.Collection;
import java.util.Optional;

/*
 * Translates to Flang's ExplicitShapeSpec, AllocateShapeSpec and AllocateCoshapeSpec.
 */
public class ExplicitShape extends FortranNode {
    public ExplicitShape(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<Expr> getLowerBound() {
        return getChildTry(Expr.class, getNumChildren() - 2);
    }

    public Expr getUpperBound() {
        return getChild(Expr.class, getNumChildren() - 1);
    }

    @Override
    public String getCode() {
        var lowerBoundCode = getLowerBound()
                .map(bound -> bound.getCode() + ":")
                .orElse("");
        var upperBoundCode = getUpperBound().getCode();
        return lowerBoundCode + upperBoundCode;
    }
}
