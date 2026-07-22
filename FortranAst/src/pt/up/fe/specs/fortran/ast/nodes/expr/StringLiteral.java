package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranAstOptions;
import pt.up.fe.specs.fortran.ast.FortranContext;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.Optional;

/**
 * R724 char-literal-constant
 */
public class StringLiteral extends KindedLiteral {
    public static final DataKey<String> CONTENTS = KeyFactory.string("contents");

    public StringLiteral(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getContents() {
        return get(CONTENTS);
    }

    @Override
    public String getCode() {
        char delimiter = get(CONTEXT).get(FortranContext.FORTRAN_OPTIONS).get(FortranAstOptions.SINGLE_QUOTE_STRINGS)
                ? '\'' : '"';

        // TODO: Check if needed
        // Escape literal according to delimiter
        var delimiterS = Character.toString(delimiter);
        var escapedString = getContents().replace(delimiterS, delimiterS + delimiterS);

        return getKindPrefix() + delimiterS + escapedString + delimiterS;
    }
}
