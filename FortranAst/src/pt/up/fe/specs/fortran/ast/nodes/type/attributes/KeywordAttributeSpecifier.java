package pt.up.fe.specs.fortran.ast.nodes.type.attributes;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class KeywordAttributeSpecifier extends AttributeSpecifier {
    // DATAKEYS BEGIN

    /**
     * The keyword of the attribute specifier, e.g., "allocatable", "pointer", etc.
     */
    public final static DataKey<String> KEYWORD = KeyFactory.string("keyword");


    // DATAKEYS END

    public KeywordAttributeSpecifier(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        return get(KEYWORD);
    }
}
