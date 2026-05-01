package pt.up.fe.specs.fortran.ast.nodes.omp.clause;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class OmpNowaitClause extends OmpClause {
    public OmpNowaitClause(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
