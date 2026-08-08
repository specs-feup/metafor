package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

/**
 * R708 int-literal-constant
 */
public class IntLiteral extends KindedLiteral {
    public static final DataKey<String> SOURCE = KeyFactory.string("source");

    public IntLiteral(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getSource() {
        return get(SOURCE);
    }

    @Override
    public String getCode() {
        return getSource() + getKindSuffix();
    }
}
