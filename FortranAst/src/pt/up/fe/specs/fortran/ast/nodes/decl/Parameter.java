package pt.up.fe.specs.fortran.ast.nodes.decl;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

/// Maps to the DummyArg node in the Flang representation
public abstract class Parameter extends FortranNode {
    public Parameter(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}

