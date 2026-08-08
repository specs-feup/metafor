package pt.up.fe.specs.fortran.ast.nodes.stmt.datastmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.Variable;
import pt.up.fe.specs.fortran.ast.nodes.expr.DataRef;

import java.util.Collection;

public class DataStmtVariable extends DataStmtObject {
    public DataStmtVariable(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Variable getVariable() {
        return getChild(Variable.class);
    }

    @Override
    public String getCode() {
        return getVariable().getCode();
    }
}
