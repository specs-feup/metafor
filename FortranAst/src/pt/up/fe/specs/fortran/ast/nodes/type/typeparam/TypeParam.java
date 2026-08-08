package pt.up.fe.specs.fortran.ast.nodes.type.typeparam;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class TypeParam extends FortranNode {
    public static final DataKey<Optional<String>> KEYWORD = KeyFactory.optional("keyword");

    public TypeParam(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<String> getKeyword() {
        return get(KEYWORD);
    }

    public TypeParamValue getValue() {
        return getChild(TypeParamValue.class, 0);
    }

    @Override
    public String getCode() {
        var keyword = getKeyword().map(k -> k + "=").orElse("");
        return keyword + getValue().getCode();
    }
}
