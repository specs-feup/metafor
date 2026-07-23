package pt.up.fe.specs.fortran.ast.nodes.alloc;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.Variable;
import pt.up.fe.specs.fortran.ast.nodes.expr.DataRef;

import java.util.Collection;

public class StatAllocOption extends AllocOption {
    public StatAllocOption(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Variable getRef() {
        return getChild(Variable.class);
    }

    @Override
    public String getCode() {
        return keyword(FortranKeyword.STAT) + "=" + getRef().getCode();
    }
}
