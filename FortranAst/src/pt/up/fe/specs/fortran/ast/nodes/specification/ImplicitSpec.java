package pt.up.fe.specs.fortran.ast.nodes.specification;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.Optional;

public class ImplicitSpec extends FortranNode {
    public static final DataKey<String> FIRST_LOCATION = KeyFactory.string("first_location");
    public static final DataKey<Optional<String>> LAST_LOCATION = KeyFactory.optional("last_location");

    public ImplicitSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getFirstLocation() {
        return get(FIRST_LOCATION);
    }
}
