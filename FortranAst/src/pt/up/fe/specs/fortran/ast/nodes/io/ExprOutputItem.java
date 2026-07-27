package pt.up.fe.specs.fortran.ast.nodes.io;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;

import java.util.Collection;

public class ExprOutputItem extends OutputItem {
    public ExprOutputItem(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Expr getExpr() {
        return getChild(Expr.class, 0);
    }

    @Override
    public String getCode() {
        return getExpr().getCode();
    }
}
