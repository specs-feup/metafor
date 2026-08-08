package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.Optional;

public abstract class KindedLiteral extends Literal {
    public static final DataKey<Optional<String>> KIND_PARAM = KeyFactory.optional("kindParam");

    public KindedLiteral(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<String> getKindParam() {
        return get(KIND_PARAM);
    }

    protected String getKindPrefix() {
        return getKindParam().map(k -> k + "_").orElseGet(() -> "");
    }

    protected String getKindSuffix() {
        return getKindParam().map(k -> "_" + k).orElseGet(() -> "");
    }
}
