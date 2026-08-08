package pt.up.fe.specs.fortran.ast.nodes.program.subprogram;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.specification.interfaces.InterfaceFunction;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;

public class EndFunctionStmt extends Stmt {
    public EndFunctionStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getFunctionName() {
        var parent = getParent();

        if (parent instanceof Function function) {
            return function.getName();
        }

        if (parent instanceof InterfaceFunction interfaceFunction) {
            return interfaceFunction.getName();
        }

        throw new RuntimeException("EndFunctionStmt must be inside a Function or InterfaceFunction, but found: " + parent.getClass().getSimpleName());
    }

    @Override
    public String getStmtCode() {
        var functionName = getFunctionName();
        return keyword(FortranKeyword.END) + " " + keyword(FortranKeyword.FUNCTION) + " " + functionName;
    }
}
