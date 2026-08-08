package pt.up.fe.specs.fortran.ast.nodes.stmt.interfaces;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;

public class EndInterfaceStmt extends Stmt {
    public EndInterfaceStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getStmtCode() {
        var genericSpecCode = getAncestor(InterfaceStmt.class).getGenericSpec()
                .map(spec -> " " + spec.getCode())
                .orElse("");

        return keyword(FortranKeyword.END) + " " + keyword(FortranKeyword.INTERFACE) + genericSpecCode;
    }
}
