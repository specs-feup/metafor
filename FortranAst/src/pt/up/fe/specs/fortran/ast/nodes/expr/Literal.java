package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

/**
 * R605 literal-constant
 */
public abstract class Literal extends Expr {
    public Literal(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
