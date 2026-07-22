package pt.up.fe.specs.fortran.ast.nodes.stmt.usestmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public abstract class Only extends FortranNode {
    public Only(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
