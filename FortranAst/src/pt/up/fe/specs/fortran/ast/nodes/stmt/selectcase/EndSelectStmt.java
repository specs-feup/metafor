package pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;

public class EndSelectStmt extends Stmt {
    public EndSelectStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getStmtCode() {
        var nameOpt = getAncestor(CaseConstruct.class).getName();

        var code = new StringBuilder();

        code.append(keyword(FortranKeyword.END))
                .append(" ")
                .append(keyword(FortranKeyword.SELECT));

        nameOpt.ifPresent(name -> code.append(" ").append(name));

        return code.toString();
    }
}
