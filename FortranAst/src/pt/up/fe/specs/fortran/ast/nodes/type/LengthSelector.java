package pt.up.fe.specs.fortran.ast.nodes.type;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;

import java.util.Collection;

public class LengthSelector extends CharSelector {
    public LengthSelector(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Expr getExpr() {
        return getChild(Expr.class);
    }

    @Override
    public String getCode() {
        return "(LEN = " + getExpr().getCode() + ")";
    }
}
