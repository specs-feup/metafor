package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.Optional;

public class SubscriptTriplet extends SectionSubscript {
    public static DataKey<Boolean> HAS_START = KeyFactory.bool("hasStart");
    public static DataKey<Boolean> HAS_END = KeyFactory.bool("hasEnd");
    public static DataKey<Boolean> HAS_STRIDE = KeyFactory.bool("hasStride");

    public SubscriptTriplet(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<Expr> getStart() {
        return get(HAS_START)
                ? Optional.of(getChild(Expr.class, 0))
                : Optional.empty();
    }

    public Optional<Expr> getEnd() {
        return get(HAS_END)
                ? Optional.of(getChild(Expr.class, get(HAS_START) ? 1 : 0))
                : Optional.empty();
    }

    public Optional<Expr> getStride() {
        return get(HAS_STRIDE)
                ? Optional.of(getChild(Expr.class, getNumChildren() - 1))
                : Optional.empty();
    }
}
