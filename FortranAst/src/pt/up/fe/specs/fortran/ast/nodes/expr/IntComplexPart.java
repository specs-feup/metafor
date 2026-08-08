package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class IntComplexPart extends ComplexPart<IntLiteral> {
    public IntComplexPart(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public IntLiteral getLiteral() {
        return getChild(IntLiteral.class, 0);
    }
}
