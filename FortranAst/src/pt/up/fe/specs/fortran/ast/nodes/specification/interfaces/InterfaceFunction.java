package pt.up.fe.specs.fortran.ast.nodes.specification.interfaces;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.subprogram.EndFunctionStmt;
import pt.up.fe.specs.fortran.ast.nodes.program.subprogram.FunctionStmt;

import java.util.Collection;

public class InterfaceFunction extends InterfaceBody {
    public InterfaceFunction(DataStore data, Collection<? extends FortranNode> children) {
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
