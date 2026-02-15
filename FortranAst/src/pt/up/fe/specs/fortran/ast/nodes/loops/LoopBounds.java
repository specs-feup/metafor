package pt.up.fe.specs.fortran.ast.nodes.loops;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;
import pt.up.fe.specs.fortran.ast.nodes.variable.DataRef;

import java.util.Collection;
import java.util.Optional;

public class LoopBounds extends FortranNode {

    public LoopBounds(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public DataRef getVar() {
        return getChild(DataRef.class);
    }

    public Expr getLower() {
        return getChild(Expr.class, 0);
    }

    public Expr getUpper() {
        return getChild(Expr.class, 1);
    }

    public Optional<Expr> getStep() {
        return getChildTry(Expr.class, 2);
    }
}
