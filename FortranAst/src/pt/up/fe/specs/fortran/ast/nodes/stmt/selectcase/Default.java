package pt.up.fe.specs.fortran.ast.nodes.stmt.selectcase;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class Default extends CaseSelector {
    public Default(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
