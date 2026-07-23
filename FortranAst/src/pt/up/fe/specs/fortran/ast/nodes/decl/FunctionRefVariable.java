package pt.up.fe.specs.fortran.ast.nodes.decl;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

// TODO(Process-ing): Develop and use this node
public class FunctionRefVariable extends Variable {
    public FunctionRefVariable(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
