package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.utils.NameValue;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.ANameValue;

public class FNameValue extends ANameValue {

    private final NameValue nameValue;

    public FNameValue(NameValue nameValue) {
        this.nameValue = nameValue;
    }

    @Override
    public String getNameImpl() {
        return nameValue.getName();
    }

    @Override
    public FortranNode getNode() {
        return nameValue;
    }
}
