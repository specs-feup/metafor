package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.List;

public class ArraySubscriptExpr extends FortranNode {

    public ArraySubscriptExpr(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public DataRef getRef() {
        return getChild(DataRef.class);
    }

    public List<Expr> getSubscripts() {
        return getChildren(Expr.class, 1);
    }
}
