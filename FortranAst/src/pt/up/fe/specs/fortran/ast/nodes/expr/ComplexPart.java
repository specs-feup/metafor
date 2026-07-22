package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public abstract class ComplexPart<LiteralT extends Literal> extends FortranNode {
    public ComplexPart(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public abstract LiteralT getLiteral();

    @Override
    public String getCode() {
        return getLiteral().getCode();
    }
}
