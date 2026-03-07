package pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class CaseRightRange extends CaseValueRange {
    public static final DataKey<Integer> RIGHT_VALUE = KeyFactory.integer("right");

    public CaseRightRange(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public int getRightValue() {
        return get(RIGHT_VALUE);
    }

    @Override
    public String getCode() {
        return ":" + getRightValue();
    }
}
