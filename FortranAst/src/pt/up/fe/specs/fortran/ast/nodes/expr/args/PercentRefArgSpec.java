package pt.up.fe.specs.fortran.ast.nodes.expr.args;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;

import java.util.Collection;

public class PercentRefArgSpec extends ArgSpec {
    public PercentRefArgSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Expr getExpr() {
        return getChild(Expr.class, 0);
    }

    @Override
    public String getCode() {
        var keywordCode = getKeywordCode();
        var exprCode = getExpr().getCode();

        return keywordCode + "%" + keyword(FortranKeyword.REF) + "(" + exprCode + ")";
    }
}
