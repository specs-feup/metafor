package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.Optional;

public class Substring extends Designator {
    public static final DataKey<Optional<Integer>> LOWER_IDX = KeyFactory.optional("lowerIdx");
    public static final DataKey<Optional<Integer>> UPPER_IDX = KeyFactory.optional("upperIdx");

    public Substring(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public DataRef getRef() {
        return getChild(DataRef.class, 0);
    }

    public Optional<Expr> getLowerBound() {
        return get(LOWER_IDX).map(idx -> getChild(Expr.class, idx));
    }

    public Optional<Expr> getUpperBound() {
        return get(UPPER_IDX).map(idx -> getChild(Expr.class, idx));
    }

    @Override
    public String getCode() {
        var refCode = getRef().getCode();
        var lowerCode = getLowerBound().map(Expr::getCode).orElse("");
        var upperCode = getUpperBound().map(Expr::getCode).orElse("");

        return refCode + "(" + lowerCode + ":" + upperCode + ")";
    }
}
