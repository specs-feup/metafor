package pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;

import java.util.Collection;

public class IfThenStmt extends FortranNode {
    public IfThenStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Expr getCondition() {
        return getChild(Expr.class, 0);
    }

    @Override
    public String getCode() {
        var condition = getCondition();

        return String.format(
                "%s (%s) %s",
                keyword(FortranKeyword.IF),
                condition.getCode(),
                keyword(FortranKeyword.THEN)
        );
    }
}
