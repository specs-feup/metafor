package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.enums.UnaryOperatorKind;

import java.util.Collection;

public class UnaryOperator extends Expr {

    public final static DataKey<UnaryOperatorKind> OP = KeyFactory.enumeration("op", UnaryOperatorKind.class);

    public UnaryOperator(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Expr getSubExpr() {
        return getChild(Expr.class);
    }

    public Expr setSubExpr(Expr newSubExpr) {
        return (Expr) setChild(0, newSubExpr);
    }

    @Override
    public String getCode() {
        return get(OP).getOpString() + getSubExpr().getCode();
    }
}
