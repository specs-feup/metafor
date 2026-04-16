package pt.up.fe.specs.fortran.ast.nodes.type;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.Optional;

/**
 * Represents Fortran integer types.
 */
public class IntegerType extends IntrinsicType {
    public static final DataKey<Optional<Integer>> KIND = KeyFactory.optional("kind");

    public IntegerType(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<Integer> getKind() {
        return get(KIND);
    }

    @Override
    public String getCode() {
        var kindOpt = getKind();

        var code = new StringBuilder();

        code.append("integer");
        kindOpt.ifPresent(kind -> code.append("(").append(kind).append(")"));

        return code.toString();
    }
}
