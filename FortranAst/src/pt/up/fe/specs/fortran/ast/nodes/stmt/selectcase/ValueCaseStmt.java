package pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ValueCaseStmt extends CaseStmt {
    public ValueCaseStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<CaseValueRange> getCaseValueRanges() {
        return getChildrenOf(CaseValueRange.class);
    }

    @Override
    public String getCode() {
        var caseValueRanges = getCaseValueRanges();

        var caseListCode = caseValueRanges.stream()
                .map(CaseValueRange::getCode)
                .collect(Collectors.joining(", "));

        return keyword(FortranKeyword.CASE) + " (" + caseListCode + ")";
    }
}
