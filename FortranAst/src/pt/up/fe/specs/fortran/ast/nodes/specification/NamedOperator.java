package pt.up.fe.specs.fortran.ast.nodes.specification;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class NamedOperator extends DefinedOperator {
    public static final DataKey<String> OPERATOR_NAME = KeyFactory.string("operator_name");

    public NamedOperator(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getOperatorName() {
        return get(OPERATOR_NAME);
    }

    @Override
    public String getCode() {
        return getOperatorName();
    }
}
