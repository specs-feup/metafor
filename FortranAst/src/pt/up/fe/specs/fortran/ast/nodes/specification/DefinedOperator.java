package pt.up.fe.specs.fortran.ast.nodes.specification;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

// NOTE: It may make sense in the future to specialize this node for each usage, since custom operators and some
// intrinsic operators are invalid in some contexts
public abstract class DefinedOperator extends FortranNode {
    public DefinedOperator(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
