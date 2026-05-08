package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpClause;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AOmpClause;

public class FOmpClause extends AOmpClause {

    public final OmpClause ompClause;

    public FOmpClause(OmpClause ompClause) {
        this.ompClause = ompClause;
    }

    @Override
    public FortranNode getNode() {
        return ompClause;
    }
}
