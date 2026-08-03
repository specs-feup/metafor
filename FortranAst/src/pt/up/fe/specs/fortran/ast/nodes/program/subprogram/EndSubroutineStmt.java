package pt.up.fe.specs.fortran.ast.nodes.program.subprogram;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;

public class EndSubroutineStmt extends Stmt {
    public EndSubroutineStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getStmtCode() {
        if (fixedForm()) {
            return keyword(FortranKeyword.END);
        }

        var subroutineName = getAncestor(Subroutine.class).getName();
        return keyword(FortranKeyword.END) + " " + keyword(FortranKeyword.SUBROUTINE) + " " + subroutineName;
    }
}
