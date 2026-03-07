package pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class CaseStmt extends FortranNode {
    public CaseStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public CaseSelector getCaseSelector() {
        return getChild(CaseSelector.class, 0);
    }

    @Override
    public String getCode() {
        var caseSelector = getCaseSelector();

        return keyword(FortranKeyword.CASE) + " " + caseSelector.getCode();
    }
}
