package pt.up.fe.specs.fortran.ast.nodes.loops;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.DataRef;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;

import java.util.Collection;
import java.util.Optional;

public class RangeLoopControl extends LoopControl {
    public RangeLoopControl(DataStore data, Collection<? extends FortranNode> children) {
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

    public Expr setUpper(Expr newUpper) {
        return (Expr) setChild(2, newUpper);
    }

    public void setStep(Expr newStep) {
        if (getStep().isPresent()) {
            setChild(3, newStep);
        } else {
            addChild(newStep);
        }
    }

    @Override
    public String getCode() {
        StringBuilder code = new StringBuilder();

        code.append(getVar().getCode())
                .append(optSpc()).append("=").append(optSpc())
                .append(getLower().getCode()).append(",").append(optSpc())
                .append(getUpper().getCode());

        getStep().ifPresent(step -> code.append(",").append(optSpc()).append(step.getCode()));

        return code.toString();
    }
}
