package pt.up.fe.specs.fortran.ast.nodes.specification.coshape;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class DeferredCoshapeSpec extends CoarraySpec {
    /**
     * Defines the number of colons in the specification
     */
    public static final DataKey<Integer> RANK = KeyFactory.integer("rank");

    public DeferredCoshapeSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public int getRank() {
        return get(RANK);
    }

    @Override
    public String getCode() {
        return "[:" + ", :".repeat(getRank() - 1) + "]";
    }
}
