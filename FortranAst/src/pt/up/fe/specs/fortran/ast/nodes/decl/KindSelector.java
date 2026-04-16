package pt.up.fe.specs.fortran.ast.nodes.decl;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class KindSelector extends FortranNode {
    public static final DataKey<Integer> KIND = KeyFactory.integer("kind");
    public static final DataKey<Boolean> LEGACY = KeyFactory.bool("legacy");

    public KindSelector(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Integer getKind() {
        return get(KIND);
    }

    public boolean isLegacy() {
        return get(LEGACY);
    }

    @Override
    public String getCode() {
        return isLegacy()
                ? "*" + getKind()
                : "(" + getKind() + ")";
    }
}
