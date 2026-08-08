package pt.up.fe.specs.fortran.ast.nodes.program.unit;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;

public class EndProgramStmt extends Stmt {
    public EndProgramStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getStmtCode() {
        // TODO(Process-ing): Remove this special case
        if (fixedForm()) {
            return keyword(FortranKeyword.END);
        }

        var nameOpt = getAncestor(MainProgram.class).getName();
        var nameSuffix = nameOpt.map(name -> " " + name).orElse("");

        return keyword(FortranKeyword.END) + " " + keyword(FortranKeyword.PROGRAM) + nameSuffix;
    }
}
