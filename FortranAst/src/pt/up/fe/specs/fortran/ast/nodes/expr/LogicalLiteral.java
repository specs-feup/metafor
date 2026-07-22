package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

/**
 * R708 int-literal-constant
 */
public class LogicalLiteral extends KindedLiteral {
    public static final DataKey<Boolean> VALUE = KeyFactory.bool("value");

    public LogicalLiteral(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public boolean getValue() {
        return get(VALUE);
    }

    @Override
    public String getCode() {
        var valueCode = encase(getValue() ? ".true." : ".false.");
        return valueCode + getKindSuffix();
    }
}
