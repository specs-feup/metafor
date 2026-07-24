package pt.up.fe.specs.fortran.ast.nodes.decl.typeparam;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class StarTypeParamValue extends TypeParamValue {
    public StarTypeParamValue(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        return "*";
    }
}
