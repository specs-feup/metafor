package pt.up.fe.specs.fortran.ast.nodes.decl;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Designator;

import java.util.Collection;

public class DesignatorVariable extends Variable {
    public DesignatorVariable(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Designator getDesignator() {
        return getChild(Designator.class, 0);
    }

    @Override
    public String getCode() {
        return getDesignator().getCode();
    }
}
