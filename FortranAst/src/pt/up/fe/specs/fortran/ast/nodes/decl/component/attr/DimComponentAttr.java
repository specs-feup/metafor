package pt.up.fe.specs.fortran.ast.nodes.decl.component.attr;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.specification.shape.ComponentArraySpec;

import java.util.Collection;

public class DimComponentAttr extends ComponentAttr {
    public DimComponentAttr(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    protected ComponentArraySpec getArraySpec() {
        return getChild(ComponentArraySpec.class, 0);
    }

    @Override
    public String getCode() {
        return keyword(FortranKeyword.DIMENSION) + getArraySpec().getCode();
    }
}
