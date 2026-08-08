package pt.up.fe.specs.fortran.ast.nodes.specification.coshape;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public abstract class CoarraySpec extends FortranNode {
    public CoarraySpec(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
