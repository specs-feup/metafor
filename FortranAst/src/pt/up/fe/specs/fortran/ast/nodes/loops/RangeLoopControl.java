package pt.up.fe.specs.fortran.ast.nodes.loops;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;
import pt.up.fe.specs.fortran.ast.nodes.expr.dataref.DataRef;

import java.util.Collection;
import java.util.Optional;

public class RangeLoopControl extends LoopControl {
    public static final DataKey<String> VARIABLE = KeyFactory.string("variable");

    public RangeLoopControl(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getVariable() {
        return get(VARIABLE);
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
        var lowerCode = getLower().getCode();
        var upperCode = getUpper().getCode();
        var stepCode = getStep().map(step -> ", " + step.getCode()).orElse("");

        return getVariable() + " = " + lowerCode + ", " + upperCode + stepCode;
    }
}
