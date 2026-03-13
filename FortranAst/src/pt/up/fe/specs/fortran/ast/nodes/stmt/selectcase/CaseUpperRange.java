package pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Literal;

import java.util.Collection;

public class CaseUpperRange extends CaseValueRange {
    public CaseUpperRange(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Literal getUpper() {
        return getChild(Literal.class, 0);
    }

    @Override
    public String getCode() {
        var upper = getUpper();

        return ":" + upper.getCode();
    }
}
