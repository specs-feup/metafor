package pt.up.fe.specs.fortran.ast.nodes.specification;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.DataRef;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;

import java.util.Collection;

public class NamedConstantDef extends FortranNode {
    public NamedConstantDef(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public DataRef getRef() {
        return (DataRef) getChild(0);
    }

    public Expr getExpr() {
        return (Expr) getChild(1);
    }

    @Override
    public String getCode() {
        return getRef().getCode() + " = " + getExpr().getCode();
    }
}
