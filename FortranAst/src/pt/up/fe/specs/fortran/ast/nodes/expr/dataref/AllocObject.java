package pt.up.fe.specs.fortran.ast.nodes.expr.dataref;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public abstract class AllocObject extends DataRef {
    public AllocObject(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
