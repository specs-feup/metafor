package pt.up.fe.specs.fortran.ast.nodes.decl;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.Optional;

public abstract class KindSelector extends FortranNode {
    public static final DataKey<Optional<Integer>> KIND = KeyFactory.optional("kind");

    public KindSelector(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<Integer> getKind() {
        return get(KIND);
    }
}
