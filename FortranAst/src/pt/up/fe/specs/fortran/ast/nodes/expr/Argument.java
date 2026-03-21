package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class Argument extends Expr {
    public Argument(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Expr getExpr() {
        return getChild(Expr.class);
    }

    @Override
    public String getCode() {
        return getExpr().getCode();
    }
}
