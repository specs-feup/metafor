package pt.up.fe.specs.fortran.ast.nodes.type.lenselector;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.type.typeparam.TypeParamValue;

import java.util.Collection;

public class ParamCharLenSelector extends CharLenSelector {
    public ParamCharLenSelector(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public TypeParamValue getLength() {
        return getChild(TypeParamValue.class, 0);
    }

    @Override
    public String getCode() {
        var lengthCode = getLength().getCode();

        return "*(" + lengthCode + ")";
    }
}
