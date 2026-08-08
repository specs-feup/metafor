package pt.up.fe.specs.fortran.ast.nodes.program.subprogram;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class Function extends InternalSubprogram {

    public Function(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getName() {
        return getFunctionStmt().getFunctionName();
    }

    public FunctionStmt getFunctionStmt() {
        return (FunctionStmt) getStartStmt();
    }

    public EndFunctionStmt getEndFunctionStmt() {
        return (EndFunctionStmt) getEndStmt();
    }
}
