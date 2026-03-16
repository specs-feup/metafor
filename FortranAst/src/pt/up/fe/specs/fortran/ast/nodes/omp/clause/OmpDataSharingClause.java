package pt.up.fe.specs.fortran.ast.nodes.omp.clause;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.DataRef;

import java.util.Collection;
import java.util.List;

public class OmpDataSharingClause extends OmpClause {
    public OmpDataSharingClause(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<DataRef> getRefs() {
        return getChildrenOf(DataRef.class);
    }
}
