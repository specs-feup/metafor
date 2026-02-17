package pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;
import pt.up.fe.specs.fortran.ast.nodes.program.StmtBlock;

import java.util.Collection;

public class ElseIfBlock extends FortranNode {
     public ElseIfBlock(DataStore data, Collection<? extends FortranNode> children) {
         super(data, children);
     }

    public Expr getCondition() {
        return getChild(Expr.class, 0);
    }

    public StmtBlock getBlock() {
        return getChild(StmtBlock.class, 1);
    }

    @Override
    public String getCode() {
        var code = new StringBuilder();

        var condition = getCondition();
        var block = getBlock();

        code.append(keyword(FortranKeyword.ELSE))
                .append(" ")
                .append(keyword(FortranKeyword.IF))
                .append(" (")
                .append(condition.getCode())
                .append(") ")
                .append(FortranKeyword.THEN)
                .append(ln());

        code.append(block.getCode());

        return code.toString();
    }
}
