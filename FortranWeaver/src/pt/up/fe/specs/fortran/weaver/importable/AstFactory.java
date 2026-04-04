package pt.up.fe.specs.fortran.weaver.importable;

import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpClause;
import pt.up.fe.specs.fortran.ast.nodes.stmt.DoStmt;
import pt.up.fe.specs.fortran.weaver.FortranJoinpoints;
import pt.up.fe.specs.fortran.weaver.FortranWeaver;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.ADoStatement;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AOmpClause;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.AOmpLoopConstruct;
import pt.up.fe.specs.util.SpecsCollections;

import java.util.List;

public class AstFactory {
    public static AOmpLoopConstruct ompLoopConstruct(ADoStatement loop, Object[] args) {
        List<OmpClause> clauses = SpecsCollections.asListT(AOmpClause.class, args)
                .stream()
                .map(clause -> (OmpClause) clause.getNode())
                .toList();

        DoStmt doStmt = (DoStmt) loop.getNode();

        return FortranJoinpoints.create(FortranWeaver.getFactory().ompLoopConstruct(doStmt, clauses), AOmpLoopConstruct.class);
    }
}
