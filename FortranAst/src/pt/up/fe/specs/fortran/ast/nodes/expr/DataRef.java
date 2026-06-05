package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.Optional;

public class DataRef extends Designator {
    // DATAKEYS BEGIN

    /**
     * The name of the entity.
     */
    public final static DataKey<String> NAME = KeyFactory.string("name");

    public final static DataKey<Optional<String>> SCOPE = KeyFactory.optional("scope");

    // DATAKEYS END

    public DataRef(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getName() {
        return get(NAME);
    }

    public Optional<String> getScope() {
        return get(SCOPE);
    }

    @Override
    public String getCode() {
        return get(NAME);
    }
}
