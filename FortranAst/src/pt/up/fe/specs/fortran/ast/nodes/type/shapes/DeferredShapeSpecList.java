package pt.up.fe.specs.fortran.ast.nodes.type.shapes;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.IntLiteral;

import java.util.Collection;
import java.util.Collections;

public class DeferredShapeSpecList extends ShapeSpecification {
    // DATAKEYS BEGIN

    /**
     * DeferredShapeSpecList is just a count of the colons (i.e., the rank).
     */
    public final static DataKey<Integer> RANK = KeyFactory.integer("rank");


    // DATAKEYS END

    public DeferredShapeSpecList(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var rank = get(RANK);

        return String.join(", ", Collections.nCopies(rank, ":"));
    }
}
