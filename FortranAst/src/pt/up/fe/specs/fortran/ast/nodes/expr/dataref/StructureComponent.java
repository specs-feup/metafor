package pt.up.fe.specs.fortran.ast.nodes.expr.dataref;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class StructureComponent extends AllocObject {
    public static final DataKey<String> COMPONENT_NAME = KeyFactory.string("component_name");

    public StructureComponent(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public DataRef getBase() {
        return getChild(DataRef.class, 0);
    }

    public String getComponentName() {
        return get(COMPONENT_NAME);
    }

    @Override
    public String getCode() {
        return getBase().getCode() + "%" + getComponentName();
    }
}
