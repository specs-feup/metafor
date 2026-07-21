package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.omp.OmpConstruct;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.enums.OmpDirectiveKind;
import pt.up.fe.specs.fortran.weaver.FortranJoinpoints;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AOmpClause;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AOmpConstruct;

import java.util.Arrays;
import java.util.List;

public class FOmpConstruct extends AOmpConstruct {

    public final OmpConstruct ompConstruct;

    public FOmpConstruct(OmpConstruct ompConstruct) {
        super(new FExecutableConstruct(ompConstruct));
        this.ompConstruct = ompConstruct;
    }

    @Override
    public AOmpClause[] getClausesArrayImpl() {
        return ompConstruct.getClauses()
                .stream()
                .map(FortranJoinpoints::create)
                .toList()
                .toArray(new AOmpClause[0]);
    }

    @Override
    public void setClausesImpl(AOmpClause[] clauses) {
        List<OmpClause> clauseList = Arrays.stream(clauses)
                .map(c -> (OmpClause) c.getNode())
                .toList();

        ompConstruct.setClauses(clauseList);
    }

    @Override
    public void setDirectiveImpl(String directive) {
        ompConstruct.set(OmpConstruct.KINDS, OmpDirectiveKind.getKinds(directive));
    }

    @Override
    public FortranNode getNode() {
        return ompConstruct;
    }
}
