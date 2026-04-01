package pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;

public class SelectCaseStmt extends Stmt {
    public SelectCaseStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Expr getExpr() {
        return getChild(Expr.class, 0);
    }

    @Override
    public String getCode() {
        var nameOpt = getAncestor(CaseConstruct.class).getName();
        var expr = getExpr();

        var code = new StringBuilder();

        nameOpt.ifPresent(name -> code.append(name).append(": "));

        code.append(keyword(FortranKeyword.SELECT))
                .append(" ")
                .append(keyword(FortranKeyword.CASE))
                .append(" (")
                .append(expr.getCode())
                .append(")");

        return code.toString();
    }
}
