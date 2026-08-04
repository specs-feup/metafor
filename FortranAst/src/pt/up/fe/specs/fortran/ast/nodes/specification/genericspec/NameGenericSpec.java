package pt.up.fe.specs.fortran.ast.nodes.specification.genericspec;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class NameGenericSpec extends GenericSpec {
    public static final DataKey<String> NAME = KeyFactory.string("name");

    public NameGenericSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getName() {
        return get(NAME);
    }

    @Override
    public String getCode() {
        return getName();
    }
}
