package pt.up.fe.specs.fortran.ast.nodes.program.subprogram;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;

public class EndFunctionStmt extends Stmt {
    public EndFunctionStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getStmtCode() {
        var functionName = getAncestor(Function.class).getName();
        return keyword(FortranKeyword.END) + " " + keyword(FortranKeyword.FUNCTION) + " " + functionName;
    }
}
