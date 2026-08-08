package pt.up.fe.specs.fortran.ast.nodes.type.lenselector;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class ConstLenSelector extends CharLenSelector {
    public static final DataKey<Long> LENGTH = KeyFactory.longInt("length");

    public ConstLenSelector(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public long getLength() {
        return get(LENGTH);
    }

    @Override
    public String getCode() {
        return "*" + getLength();
    }
}
