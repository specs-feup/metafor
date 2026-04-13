package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.omp.OmpConstruct;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpClause;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AOmpClause;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AOmpConstruct;

import java.util.Arrays;
import java.util.List;

public class FOmpConstruct extends AOmpConstruct {

    public final OmpConstruct ompConstruct;

    public FOmpConstruct(OmpConstruct ompConstruct) {
        super(new FExecutableStatement(ompConstruct));
        this.ompConstruct = ompConstruct;
    }

    @Override
    public void setClausesImpl(AOmpClause[] clauses) {
        List<OmpClause> clauseList = Arrays.stream(clauses)
                .map(c -> (OmpClause) c.getNode())
                .toList();

        ompConstruct.setClauses(clauseList);
    }

    @Override
    public FortranNode getNode() {
        return ompConstruct;
    }
}
