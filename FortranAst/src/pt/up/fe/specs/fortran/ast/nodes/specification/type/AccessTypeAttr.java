package pt.up.fe.specs.fortran.ast.nodes.specification.type;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.specification.enums.AccessKind;

import java.util.Collection;

public class AccessTypeAttr extends TypeAttr {
    public static final DataKey<AccessKind> ACCESS_KIND = KeyFactory.enumeration("access_kind", AccessKind.class);

    public AccessTypeAttr(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public AccessKind getAccessKind() {
        return get(ACCESS_KIND);
    }

    @Override
    public String getCode() {
        return encase(getAccessKind().name());
    }
}
