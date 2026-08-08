package pt.up.fe.specs.fortran.ast.nodes.program.unit;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;

public class EndModuleStmt extends Stmt {
    public EndModuleStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getStmtCode() {
        var moduleName = getAncestor(Module.class).getName();

        return keyword(FortranKeyword.END) + " " + keyword(FortranKeyword.MODULE) + " " + moduleName;
    }
}
