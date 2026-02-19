package pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;

import java.util.Collection;

/**
 * R1135 if-then-stmt
 */
public class IfThenStmt extends FortranNode {
    public IfThenStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Expr getCondition() {
        return getChild(Expr.class, 0);
    }

    @Override
    public String getCode() {
        var nameOpt = ((IfConstruct) getParent().getParent()).getName();
        var condition = getCondition();

        var code = new StringBuilder();

        nameOpt.ifPresent(name -> code.append(name).append(": "));

        code.append(keyword(FortranKeyword.IF))
                .append(" (")
                .append(condition.getCode())
                .append(") ")
                .append(keyword(FortranKeyword.THEN));

        return code.toString();
    }
}
