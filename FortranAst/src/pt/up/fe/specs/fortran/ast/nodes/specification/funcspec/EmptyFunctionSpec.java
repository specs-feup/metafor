package pt.up.fe.specs.fortran.ast.nodes.specification.funcspec;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.specification.enums.EmptyFunctionSpecKind;

import java.util.Collection;

public class EmptyFunctionSpec extends FunctionSpec {
    public static final DataKey<EmptyFunctionSpecKind> KIND = KeyFactory.enumeration("kind", EmptyFunctionSpecKind.class);

    public EmptyFunctionSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public EmptyFunctionSpecKind getKind() {
        return get(KIND);
    }

    @Override
    public String getCode() {
        return encase(getKind().name());
    }
}
