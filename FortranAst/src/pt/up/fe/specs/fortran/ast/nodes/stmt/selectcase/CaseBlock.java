package pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.StmtBlock;

import java.util.Collection;

public class CaseBlock extends FortranNode {
    public CaseBlock(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public CaseStmt getCaseStmt() {
        return getChild(CaseStmt.class, 0);
    }

    public StmtBlock getStmtBlock() {
        return getChild(StmtBlock.class, 1);
    }
}
