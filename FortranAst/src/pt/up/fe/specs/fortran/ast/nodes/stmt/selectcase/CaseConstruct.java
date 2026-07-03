package pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ExecutableStmt;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class CaseConstruct extends ExecutableStmt {
    public static final DataKey<Optional<String>> NAME = KeyFactory.optional("name");

    public CaseConstruct(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<String> getName() {
        return get(NAME);
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

    @Override
    public String getStmtCode() {
        var selectCaseStmt = getSelectCaseStmt();
        var caseBlocks = getCaseBlocks();
        var endSelectStmt = getEndSelectStmt();

        var code = new StringBuilder();

        // select case statement
        code.append(selectCaseStmt.getCode()).append(ln());

        // case blocks
        caseBlocks.forEach(block -> code.append(block.getCode()).append(ln()));

        // end select statement
        code.append(endSelectStmt.getCode());

        return code.toString();
    }
}
