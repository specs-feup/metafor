package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpDataSharingClause;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AOmpDataSharingClause;

public class FOmpDataSharingClause extends AOmpDataSharingClause {

    public final OmpDataSharingClause ompDataSharingClause;

    public FOmpDataSharingClause(OmpDataSharingClause ompDataSharingClause) {
        super(new FOmpClause(ompDataSharingClause));
        this.ompDataSharingClause = ompDataSharingClause;
    }

    @Override
    public FortranNode getNode() {
        return ompDataSharingClause;
    }
}
