package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class RealComplexPart extends ComplexPart<RealLiteral> {
    public RealComplexPart(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public RealLiteral getLiteral() {
        return getChild(RealLiteral.class, 0);
    }
}
