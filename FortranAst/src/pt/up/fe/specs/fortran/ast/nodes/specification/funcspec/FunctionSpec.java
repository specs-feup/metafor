package pt.up.fe.specs.fortran.ast.nodes.specification.funcspec;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

/// Maps to a PrefixSpec in the Flang AST
public abstract class FunctionSpec extends FortranNode {
    public FunctionSpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
