package pt.up.fe.specs.fortran.ast.nodes.io;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.Variable;

import java.util.Collection;

public class VarInputItem extends InputItem {
    public VarInputItem(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Variable getVariable() {
        return getChild(Variable.class, 0);
    }

    @Override
    public String getCode() {
        return getVariable().getCode();
    }
}
