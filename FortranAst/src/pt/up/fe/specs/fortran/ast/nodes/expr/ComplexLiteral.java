package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class ComplexLiteral extends Literal {
    public ComplexLiteral(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public ComplexPart<?> getRealPart() {
        return getChild(ComplexPart.class, 0);
    }

    public ComplexPart<?> getImaginaryPart() {
        return getChild(ComplexPart.class, 1);
    }

    public String getCode() {
        var real = getRealPart();
        var imag = getImaginaryPart();

        return "(" + real.getCode() + ", " + imag.getCode() + ")";
    }
}
