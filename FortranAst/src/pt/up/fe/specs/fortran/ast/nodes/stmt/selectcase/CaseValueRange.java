package pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public abstract class CaseValueRange extends FortranNode {
    public CaseValueRange(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
