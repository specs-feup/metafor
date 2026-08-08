package pt.up.fe.specs.fortran.ast.nodes.type.attributes;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranContext;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.type.attributes.enums.IntentKind;

import java.util.Collection;

import static pt.up.fe.specs.fortran.ast.FortranKeyword.INTENT;

public class IntentSpec extends AttributeSpecifier {

    public final static DataKey<IntentKind> KIND = KeyFactory.enumeration("kind", IntentKind.class);

    public IntentSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public IntentKind getKind() {
        return get(KIND);
    }

    @Override
    public String getCode() {
        var kind = getKind();
        var lowercase = getContext().get(FortranContext.FORTRAN_KEYWORDS).isLowercase();

        return keyword(INTENT) + "(" + kind.getCode(lowercase) + ")";
    }
}
