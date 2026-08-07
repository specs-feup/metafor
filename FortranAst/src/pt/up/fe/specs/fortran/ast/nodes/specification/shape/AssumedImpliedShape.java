package pt.up.fe.specs.fortran.ast.nodes.specification.shape;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;

import java.util.Collection;
import java.util.Optional;

public class AssumedImpliedShape extends FortranNode {
    public AssumedImpliedShape(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<Expr> getLowerBound() {
        return getChildTry(Expr.class, 0);
    }

    @Override
    public String getCode() {
        var lowerBoundCode = getLowerBound()
                .map(bound -> bound.getCode() + ":")
                .orElse("");
        return lowerBoundCode + "*";
    }
}
