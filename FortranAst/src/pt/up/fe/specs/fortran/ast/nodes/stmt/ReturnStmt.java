package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;

import java.util.Collection;
import java.util.Optional;

public class ReturnStmt extends ActionStmt{
    public ReturnStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<Expr> getExpr() {
        return getChildTry(Expr.class);
    }

    @Override
    public String getCode() {
        StringBuilder code = new StringBuilder();

        code.append(FortranKeyword.RETURN.getKeyword(false));

        getExpr().ifPresent(expr -> code.append(" ").append(expr.getCode()));

        return code.toString();
    }
}
