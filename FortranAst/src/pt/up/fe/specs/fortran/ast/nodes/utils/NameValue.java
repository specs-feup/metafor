package pt.up.fe.specs.fortran.ast.nodes.utils;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.Optional;

public class NameValue extends FortranNode {

    public final static DataKey<String> NAME = KeyFactory.string("name");

    public final static DataKey<Optional<Integer>> VALUE = KeyFactory.optional("value");

    public NameValue(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getName() {
        return get(NAME);
    }

    @Override
    public String getCode() {
        StringBuilder code = new StringBuilder();

        code.append(get(NAME));

        get(VALUE).ifPresent(value -> code.append("=").append(value));

        return code.toString();
    }
}
