package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.enums.BinaryOperatorKind;

import java.util.Collection;

public class BinaryOperator extends Expr {

    public final static DataKey<BinaryOperatorKind> OP = KeyFactory.enumeration("op", BinaryOperatorKind.class);

    public BinaryOperator(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Expr getLhs() {
        return getChild(Expr.class, 0);
    }

    public Expr getRhs() {
        return getChild(Expr.class, 1);
    }

    public Expr setLhs(Expr newLhs) {
        return (Expr) setChild(0, newLhs);
    }

    public Expr setRhs(Expr newRhs) {
        return (Expr) setChild(1, newRhs);
    }

    public BinaryOperatorKind getOp() {
        return get(OP);
    }

    @Override
    public String getCode() {

        return getLhs().getCode() +

                " " + get(OP).getOpString() + " " +

                getRhs().getCode();
    }
}
