package pt.up.fe.specs.fortran.ast.nodes.stmt.ifstmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ActionStmt;

import java.util.Collection;

public class IfStmt extends ActionStmt {
    public IfStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Expr getCondition() {
        return getChild(Expr.class, 0);
    }

    public ActionStmt getThenAction() {
        return getChild(ActionStmt.class, 1);
    }

    @Override
    public String getCode() {
        var condition = getCondition();
        var thenAction = getThenAction();

        return String.format(
                "%s (%s) %s",
                keyword(FortranKeyword.IF),
                condition.getCode(),
                thenAction.getCode()
        );
    }
}
