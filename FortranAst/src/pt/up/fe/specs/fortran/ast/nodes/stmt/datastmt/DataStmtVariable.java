package pt.up.fe.specs.fortran.ast.nodes.stmt.datastmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.DataRef;

import java.util.Collection;

public class DataStmtVariable extends DataStmtObject {
    public DataStmtVariable(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public DataRef getVariable() {
        return getChild(DataRef.class);
    }

    @Override
    public String getCode() {
        var variable = getVariable();

        return variable.getCode();
    }
}
