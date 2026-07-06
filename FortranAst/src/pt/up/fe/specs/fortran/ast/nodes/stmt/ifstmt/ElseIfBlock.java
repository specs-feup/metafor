package pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;
import pt.up.fe.specs.fortran.ast.nodes.program.StmtBlock;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;

public class ElseIfBlock extends Stmt {
     public ElseIfBlock(DataStore data, Collection<? extends FortranNode> children) {
         super(data, children);
     }

    public ElseIfStmt getElseIfStmt() {
        return getChild(ElseIfStmt.class, 0);
    }

    public Execution getBlock() {
        return getChild(Execution.class, 1);
    }

    @Override
    public String getStmtCode() {
        var elseIfStmt = getElseIfStmt();
        var block = getBlock();

        return elseIfStmt.getCode() + ln() + indent(block.getCode());
    }
}
