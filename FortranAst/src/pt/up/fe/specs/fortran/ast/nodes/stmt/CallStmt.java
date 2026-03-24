package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Call;

import java.util.Collection;

public class CallStmt extends ActionStmt {
    public CallStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Call getCall() {
        return getChild(Call.class);
    }

    @Override
    public String getCode() {
        return "call " + getCall().getCode();
    }
}
