package pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;
import pt.up.fe.specs.fortran.ast.nodes.program.StmtBlock;

import java.util.Collection;

public class IfThenBlock extends FortranNode {
    public IfThenBlock(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public IfThenStmt getIfThenStmt() {
        return getChild(IfThenStmt.class, 0);
    }

    public Execution getBlock() {
        return getChild(Execution.class, 1);
    }

    @Override
    public String getCode() {
        var ifThenStmt = getIfThenStmt();
        var block = getBlock();

        return ifThenStmt.getCode() + ln() + indent(block.getCode());
    }
}
