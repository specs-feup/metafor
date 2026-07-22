package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class NamedLiteral extends Literal {
    public NamedLiteral(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getName() {
        return getLiteral();
    }

    public String getCode() {
        return getLiteral();
    }
}
