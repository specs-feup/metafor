package pt.up.fe.specs.fortran.ast.nodes.io;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public abstract class Format extends FortranNode {
    public Format(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
