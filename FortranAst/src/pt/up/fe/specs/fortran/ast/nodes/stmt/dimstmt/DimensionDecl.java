package pt.up.fe.specs.fortran.ast.nodes.stmt.dimstmt;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.specification.shape.ArraySpec;

import java.util.Collection;

public class DimensionDecl extends FortranNode {
    public static final DataKey<String> NAME = KeyFactory.string("name");

    public DimensionDecl(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getName() {
        return get(NAME);
    }

    public ArraySpec getArraySpecification() {
        return getChild(ArraySpec.class);
    }

    @Override
    public String getCode() {
        return getName() + getArraySpecification().getCode();
    }
}
