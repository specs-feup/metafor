package pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class DefaultCaseStmt extends CaseStmt {
    public DefaultCaseStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getStmtCode() {
        var nameOpt = getAncestor(CaseConstruct.class).getName();

        var code = new StringBuilder();

        code.append(keyword(FortranKeyword.CASE))
                .append(" ")
                .append(keyword(FortranKeyword.DEFAULT));

        nameOpt.ifPresent(name -> code.append(" ").append(name));

        return code.toString();
    }
}
