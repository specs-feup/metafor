package pt.up.fe.specs.fortran.ast.nodes.program;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;

public class ProgramStmt extends Stmt {
    public ProgramStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getStmtCode() {
        var nameOpt = getAncestor(MainProgram.class).getName();
        var nameSuffix = nameOpt.map(name -> " " + name).orElse("");

        return keyword(FortranKeyword.PROGRAM) + nameSuffix;
    }
}
