package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class NamedComplexPart extends ComplexPart<NamedLiteral> {
    public NamedComplexPart(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public NamedLiteral getLiteral() {
        return getChild(NamedLiteral.class, 0);
    }
}
