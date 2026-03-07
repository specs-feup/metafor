package pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class CaseLeftRange extends CaseValueRange {
    public static final DataKey<Integer> LEFT_VALUE = KeyFactory.integer("left");

    public CaseLeftRange(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public int getLeftValue() {
        return get(LEFT_VALUE);
    }

    @Override
    public String getCode() {
        return getLeftValue() + ":";
    }
}
