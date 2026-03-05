package pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ExecutableStmt;

import java.util.Collection;
import java.util.List;

public class CaseConstruct extends ExecutableStmt {
    public CaseConstruct(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public SelectCaseStmt getSelectCaseStmt() {
        return getChild(SelectCaseStmt.class, 0);
    }

    public List<CaseBlock> getCaseBlocks() {
        return getChildrenOf(CaseBlock.class);
    }

    public EndSelectStmt getEndSelectStmt() {
        return getChild(EndSelectStmt.class);
    }
}
