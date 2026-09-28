package pt.up.fe.specs.fortran.ast.nodes.expr.dataref;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.enums.ScopeKind;

import java.util.Collection;
import java.util.Optional;

public class NameDataRef extends AllocObject {
    public static final DataKey<String> NAME = KeyFactory.string("name");
    public static final DataKey<Optional<ScopeKind>> SCOPE = KeyFactory.optional("scope");

    public NameDataRef(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getName() {
        return get(NAME);
    }

    public Optional<ScopeKind> getScope() {
        return get(SCOPE);
    }

    @Override
    public String getCode() {
        return get(NAME);
    }
}
