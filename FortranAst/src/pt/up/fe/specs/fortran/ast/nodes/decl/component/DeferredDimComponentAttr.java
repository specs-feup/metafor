package pt.up.fe.specs.fortran.ast.nodes.decl.component;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.specification.shape.ArraySpec;
import pt.up.fe.specs.fortran.ast.nodes.specification.shape.DeferredShapeSpec;

import java.util.Collection;

public class DeferredDimComponentAttr extends DimComponentAttr {
    public DeferredDimComponentAttr(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public DeferredShapeSpec getDeferredShapeSpec() {
        return getChild(DeferredShapeSpec.class, 0);
    }

    @Override
    protected ArraySpec getArraySpec() {
        return getDeferredShapeSpec();
    }
}
