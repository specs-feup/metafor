package pt.up.fe.specs.fortran.ast.nodes.type.attributes;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.specification.ArraySpecification;

import java.util.Collection;

public class DimensionSpec extends  AttributeSpecifier {
    public DimensionSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var arraySpec = getArraySpecification();
        return "dimension" + arraySpec.getCode();
    }

    private ArraySpecification getArraySpecification() {
        return getChild(ArraySpecification.class, 0);
    }
}
