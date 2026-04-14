package pt.up.fe.specs.fortran.weaver.importable;

import pt.up.fe.specs.fortran.ast.nodes.expr.DataRef;
import pt.up.fe.specs.fortran.ast.nodes.omp.OmpLoopConstruct;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpDataSharingClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.enums.OmpClauseKind;
import pt.up.fe.specs.fortran.ast.nodes.stmt.DoStmt;
import pt.up.fe.specs.fortran.weaver.FortranJoinpoints;
import pt.up.fe.specs.fortran.weaver.FortranWeaver;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.*;
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

    public static AOmpLoopConstruct emptyOmpLoopConstruct() {
        return FortranJoinpoints.create(FortranWeaver.getFactory().emptyOmpLoopConstruct(), AOmpLoopConstruct.class);
    }

    public static AOmpDataSharingClause ompPrivateClause(Object[] args) {
        List<DataRef> dataRefs = SpecsCollections.asListT(ADataRef.class, args)
                .stream()
                .map(clause -> (DataRef) clause.getNode())
                .toList();

        return FortranJoinpoints.create(FortranWeaver.getFactory().ompDataSharingClause(OmpClauseKind.PRIVATE, dataRefs), AOmpDataSharingClause.class);
    }

    public static ADataRef dataRef(String name) {
        return FortranJoinpoints.create(FortranWeaver.getFactory().dataRef(name), ADataRef.class);
    }
}
