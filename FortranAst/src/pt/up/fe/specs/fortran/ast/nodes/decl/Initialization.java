package pt.up.fe.specs.fortran.ast.nodes.decl;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public abstract class Initialization extends FortranNode {
    public Initialization(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
