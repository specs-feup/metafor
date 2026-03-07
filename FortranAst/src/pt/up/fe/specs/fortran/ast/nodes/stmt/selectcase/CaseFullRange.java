package pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Literal;

import java.util.Collection;

public class CaseFullRange extends CaseValueRange {
    public CaseFullRange(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Literal getLeftValue() {
        return getChild(Literal.class, 0);
    }

    public Literal getRightValue() {
        return getChild(Literal.class, 1);
    }

    @Override
    public String getCode() {
        var left = getLeftValue();
        var right = getRightValue();

        return left.getCode() + ":" + right.getCode();
    }
}
