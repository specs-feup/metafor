package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.Optional;

public class SubscriptTriplet extends SectionSubscript {
    public SubscriptTriplet(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Expr getStart() {
        return getChild(Expr.class, 0);
    }

    public Expr getEnd() {
        return getChild(Expr.class, 1);
    }

    public Optional<Expr> getStride() {
        return getNumChildren() > 2
                ? Optional.of(getChild(Expr.class, 2))
                : Optional.empty();
    }
}
