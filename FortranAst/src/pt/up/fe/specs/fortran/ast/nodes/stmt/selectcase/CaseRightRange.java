package pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Literal;

import java.util.Collection;

public class CaseRightRange extends CaseValueRange {
    public CaseRightRange(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Literal getRightValue() {
        return getChild(Literal.class, 0);
    }

    @Override
    public String getCode() {
        var right = getRightValue();

        return ":" + right.getCode();
    }
}
