package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.List;

public class NamelistGroup extends FortranNode {
    public static final DataKey<String> GROUP_NAME = KeyFactory.string("groupName");
    public static final DataKey<List<String>> OBJECT_NAMES = KeyFactory.list("objectNames", String.class);

    public NamelistGroup(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getGroupName() {
        return get(GROUP_NAME);
    }

    public List<String> getObjectNames() {
        return get(OBJECT_NAMES);
    }

    @Override
    public String getCode() {
        var objectsCode = String.join(", ", getObjectNames());

        return "/" + getGroupName() + "/ " + objectsCode;
    }
}
