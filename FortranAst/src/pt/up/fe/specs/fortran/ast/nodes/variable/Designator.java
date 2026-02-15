package pt.up.fe.specs.fortran.ast.nodes.variable;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class Designator extends Variable{
    public Designator(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
