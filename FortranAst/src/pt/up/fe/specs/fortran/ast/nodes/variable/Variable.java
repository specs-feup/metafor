package pt.up.fe.specs.fortran.ast.nodes.variable;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class Variable extends FortranNode {
    public Variable(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
