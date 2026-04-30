package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.Optional;

/**
 * R708 int-literal-constant
 */
public class LogicalLiteral extends Literal {

    // DATAKEYS BEGIN

    /**
     * A prefix indicating the kind of the string (e.g., encoding)
     */
    public final static DataKey<Optional<String>> KIND_PARAM = KeyFactory.optional("kindParam");


    // DATAKEYS END

    public LogicalLiteral(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public boolean getValue() {
        return get(SOURCE_LITERAL).equals("1");
    }

    @Override
    public String getCode() {
        if (getLiteral().equals("1")) {
            return ".true.";
        } else {
            return ".false.";
        }
    }
}
