package pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.StmtBlock;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;

public class ElseBlock extends FortranNode {
    public ElseBlock(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public ElseStmt getElseStmt() {
        return getChild(ElseStmt.class, 0);
    }

    public StmtBlock getBlock() {
        return getChild(StmtBlock.class, 1);
    }

    @Override
    public String getCode() {
        var elseStmt = getElseStmt();
        var block = getBlock();

        return elseStmt.getCode() + ln() + indent(block.getCode());
    }
}
