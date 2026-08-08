package pt.up.fe.specs.fortran.ast.nodes.type.attributes;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.specification.shape.ArraySpec;

import java.util.Collection;

public class DimensionSpec extends AttributeSpecifier {
    public DimensionSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var arraySpec = getArraySpecification();
        return keyword(FortranKeyword.DIMENSION) + arraySpec.getCode();
    }

    private ArraySpec getArraySpecification() {
        return getChild(ArraySpec.class, 0);
    }
}
