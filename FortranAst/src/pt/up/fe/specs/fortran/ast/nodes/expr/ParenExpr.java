package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class ParenExpr extends Expr {
    public ParenExpr(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Expr getSubExpr() {
        return getChild(Expr.class, 0);
    }

    @Override
    public String getCode() {
        return "(" + getSubExpr().getCode() + ")";
    }
}
