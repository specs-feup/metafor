package pt.up.fe.specs.fortran.ast.nodes.alloc;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.DataRef;

import java.util.Collection;

public class StatVariable extends AllocOption {
    public StatVariable(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public DataRef getRef() {
        return getChild(DataRef.class);
    }

    @Override
    public String getCode() {
        return "STAT=" + getRef().getCode();
    }
}
