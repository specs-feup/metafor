package pt.up.fe.specs.fortran.ast.nodes.io;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;

import java.util.Collection;
import java.util.Optional;

public class IoImpliedDoControl extends FortranNode {
    public static final DataKey<String> VARIABLE = KeyFactory.string("variable");

    public IoImpliedDoControl(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getVariable() {
        return get(VARIABLE);
    }

    public Expr getLowerBound() {
        return getChild(Expr.class, 0);
    }

    public Expr getUpperBound() {
        return getChild(Expr.class, 1);
    }

    public Optional<Expr> getStep() {
        return getChildTry(Expr.class, 2);
    }

    @Override
    public String getCode() {
        var variableCode = getVariable();
        var lowerBoundCode = getLowerBound().getCode();
        var upperBoundCode = getUpperBound().getCode();
        var stepCode = getStep()
                .map(step -> ", " + step.getCode())
                .orElse("");

        return variableCode + " = " + lowerBoundCode + ", " + upperBoundCode + stepCode;
    }
}
