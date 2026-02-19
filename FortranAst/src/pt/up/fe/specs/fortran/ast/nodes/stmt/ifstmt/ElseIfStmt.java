package pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;

import java.util.Collection;

public class ElseIfStmt extends FortranNode {
    public ElseIfStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Expr getCondition() {
        return getChild(Expr.class, 0);
    }

    public String getCode() {
        var condition = getCondition();
        var nameOpt = ((IfConstruct) getParent().getParent()).getName();

        var code = new StringBuilder();

        code.append(keyword(FortranKeyword.ELSE))
                .append(" ")
                .append(keyword(FortranKeyword.IF))
                .append(" (")
                .append(condition.getCode())
                .append(") ")
                .append(keyword(FortranKeyword.THEN));

        nameOpt.ifPresent(name -> code.append(" ").append(name));

        return code.toString();
    }
}
