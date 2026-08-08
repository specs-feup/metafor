package pt.up.fe.specs.fortran.ast.nodes.type.typeparam;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class DeferredTypeParamValue extends TypeParamValue {
    public DeferredTypeParamValue(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        return ":";
    }
}
