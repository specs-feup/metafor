package pt.up.fe.specs.fortran.ast.nodes.utils;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.utils.enums.IntentKind;

import java.util.Collection;

public class IntentSpec extends FortranNode {

    public final static DataKey<IntentKind> KIND = KeyFactory.enumeration("kind", IntentKind.class);

    public IntentSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public IntentKind getKind() {
        return get(KIND);
    }

    @Override
    public String getCode() {
        return "intent(" + getKind().getString().toLowerCase() + ")";
    }
}
