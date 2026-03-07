package pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Literal;

import java.util.Collection;

public class CaseValue extends CaseValueRange {
    public CaseValue(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Literal getValue() {
        return getChild(Literal.class, 0);
    }

    @Override
    public String getCode() {
        return getValue().getCode();
    }
}
