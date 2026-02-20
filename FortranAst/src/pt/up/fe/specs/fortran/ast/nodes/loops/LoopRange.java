package pt.up.fe.specs.fortran.ast.nodes.loops;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.DataRef;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;

import java.util.Collection;
import java.util.Optional;

public class LoopRange extends ALoopControl {
    public LoopRange(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public DataRef getVar() {
        return getChild(DataRef.class, 0);
    }

    public Expr getLower() {
        return getChild(Expr.class, 1);
    }

    public Expr getUpper() {
        return getChild(Expr.class, 2);
    }

    public Optional<Expr> getStep() {
        return getChildTry(Expr.class, 3);
    }

    @Override
    public String getCode() {
        StringBuilder code = new StringBuilder();

        code.append(getVar().getCode()).append(" = ")
                .append(getLower().getCode()).append(", ")
                .append(getUpper().getCode());

        getStep().ifPresent(step -> code.append(", ").append(step.getCode()));

        return code.toString();
    }
}
