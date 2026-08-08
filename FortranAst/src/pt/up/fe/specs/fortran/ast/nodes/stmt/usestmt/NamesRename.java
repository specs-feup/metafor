package pt.up.fe.specs.fortran.ast.nodes.stmt.usestmt;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class NamesRename extends Rename {
    public static final DataKey<String> LOCAL_NAME = KeyFactory.string("localName");
    public static final DataKey<String> GLOBAL_NAME = KeyFactory.string("globalName");

    public NamesRename(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getLocalName() {
        return get(LOCAL_NAME);
    }

    public String getGlobalName() {
        return get(GLOBAL_NAME);
    }

    @Override
    public String getCode() {
        var localName = getLocalName();
        var globalName = getGlobalName();

        return localName + " => " + globalName;
    }
}
