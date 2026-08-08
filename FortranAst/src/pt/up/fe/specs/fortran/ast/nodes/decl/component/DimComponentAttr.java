package pt.up.fe.specs.fortran.ast.nodes.decl.component;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.specification.shape.ArraySpec;

import java.util.Collection;

public abstract class DimComponentAttr extends ComponentAttr {
    public DimComponentAttr(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    protected ArraySpec getArraySpec() {
        return getChild(ArraySpec.class, 0);
    }

    @Override
    public String getCode() {
        return keyword(FortranKeyword.DIMENSION) + getArraySpec().getCode();
    }
}
