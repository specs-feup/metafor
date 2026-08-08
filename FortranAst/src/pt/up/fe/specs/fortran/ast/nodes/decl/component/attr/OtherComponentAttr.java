package pt.up.fe.specs.fortran.ast.nodes.decl.component.attr;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.enums.ComponentAttrKind;

import java.util.Collection;

public class OtherComponentAttr extends ComponentAttr {
    public static final DataKey<ComponentAttrKind> KIND = KeyFactory.enumeration("kind", ComponentAttrKind.class);

    public OtherComponentAttr(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public ComponentAttrKind getKind() {
        return get(KIND);
    }

    @Override
    public String getCode() {
        return encase(getKind().name());
    }
}
