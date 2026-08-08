package pt.up.fe.specs.fortran.ast.nodes.specification.shape;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class DeferredShapeSpec extends ComponentArraySpec {
    /**
     * DeferredShapeSpecList is just a count of the colons (i.e., the rank).
     */
    public final static DataKey<Integer> RANK = KeyFactory.integer("rank");

    public DeferredShapeSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public int getRank() {
        return get(RANK);
    }

    @Override
    public String getCode() {
        return "(:" + ", :".repeat(getRank() - 1) + ")";
    }
}
