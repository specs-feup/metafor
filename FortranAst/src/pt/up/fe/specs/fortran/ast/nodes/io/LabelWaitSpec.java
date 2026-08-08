package pt.up.fe.specs.fortran.ast.nodes.io;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.io.enums.LabelWaitSpecKind;

import java.util.Collection;

public class LabelWaitSpec extends WaitSpec {
    public static final DataKey<LabelWaitSpecKind> KIND = KeyFactory.enumeration("kind", LabelWaitSpecKind.class);
    public static final DataKey<Integer> LABEL = KeyFactory.integer("label");

    public LabelWaitSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public LabelWaitSpecKind getKind() {
        return get(KIND);
    }

    public int getLabel() {
        return get(LABEL);
    }

    @Override
    public String getCode() {
        return encase(getKind().name()) + "=" + getLabel();
    }
}
