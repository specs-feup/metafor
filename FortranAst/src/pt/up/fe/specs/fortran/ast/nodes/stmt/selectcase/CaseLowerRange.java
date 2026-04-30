package pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Literal;

import java.util.Collection;

public class CaseLowerRange extends CaseValueRange {
    public CaseLowerRange(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Literal getLower() {
        return getChild(Literal.class, 0);
    }

    @Override
    public String getCode() {
        var lower = getLower();

        return lower.getCode() + ":";
    }
}
