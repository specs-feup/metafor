package pt.up.fe.specs.fortran.ast.nodes.program.subprogram;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.specification.interfaces.InterfaceSubroutine;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;

public class EndSubroutineStmt extends Stmt {
    public EndSubroutineStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getSubroutineName() {
        var parent = getParent();

        if (parent instanceof Subroutine subroutine) {
            return subroutine.getName();
        }

        if (parent instanceof InterfaceSubroutine interfaceSubroutine) {
            return interfaceSubroutine.getName();
        }

        throw new RuntimeException("EndSubroutineStmt must be inside a Subroutine or InterfaceSubroutine, but found: " + parent.getClass().getSimpleName());
    }

    @Override
    public String getStmtCode() {
        // TODO(Process-ing): Remove this special case
        if (fixedForm()) {
            return keyword(FortranKeyword.END);
        }

        var subroutineName = getSubroutineName();
        return keyword(FortranKeyword.END) + " " + keyword(FortranKeyword.SUBROUTINE) + " " + subroutineName;
    }
}
