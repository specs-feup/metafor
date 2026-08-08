package pt.up.fe.specs.fortran.ast.nodes.decl.component;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.specification.shape.ArraySpec;
import pt.up.fe.specs.fortran.ast.nodes.specification.shape.ExplicitShapeArraySpec;

import java.util.Collection;

public class ExplicitDimComponentAttr extends DimComponentAttr {
    public ExplicitDimComponentAttr(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public ExplicitShapeArraySpec getExplicitShapeArraySpec() {
        return getChild(ExplicitShapeArraySpec.class, 0);
    }

    @Override
    protected ArraySpec getArraySpec() {
        return getExplicitShapeArraySpec();
    }
}
