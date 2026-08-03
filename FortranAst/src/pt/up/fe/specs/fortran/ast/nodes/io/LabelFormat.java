package pt.up.fe.specs.fortran.ast.nodes.io;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class LabelFormat extends Format {
    public static final DataKey<Integer> LABEL = KeyFactory.integer("label");

    public LabelFormat(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public int getLabel() {
        return get(LABEL);
    }

    @Override
    public String getCode() {
        return Integer.toString(getLabel());
    }
}
