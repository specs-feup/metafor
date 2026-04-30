package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.expr.BinaryOperator;
import pt.up.fe.specs.fortran.ast.nodes.expr.enums.BinaryOperatorKind;
import pt.up.fe.specs.fortran.ast.nodes.omp.OmpBlockConstruct;
import pt.up.fe.specs.fortran.ast.nodes.omp.OmpConstruct;
import pt.up.fe.specs.fortran.ast.nodes.omp.OmpLoopConstruct;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpDataSharingClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.enums.OmpClauseKind;
import pt.up.fe.specs.fortran.ast.nodes.omp.enums.OmpDirectiveKind;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;
import pt.up.fe.specs.fortran.ast.nodes.stmt.DoStmt;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

import java.util.List;

public class OmpProcessors extends ANodeProcessor {
    public OmpProcessors(FortranJsonResult data) {
        super(data);
    }

    public void ompBlockConstruct(OmpBlockConstruct ompBlockConstruct) {
        String directive = attributes().getString(ompBlockConstruct, "directive", FlangName.OMP_BEGIN_BLOCK_DIRECTIVE, FlangName.OMP_BLOCK_DIRECTIVE);
        String clauseList = attributes().getString(ompBlockConstruct, "id", FlangName.OMP_BEGIN_BLOCK_DIRECTIVE, FlangName.OMP_CLAUSE_LIST);

        List<OmpDirectiveKind> kinds = OmpDirectiveKind.getKinds(directive);

        ompBlockConstruct.set(OmpBlockConstruct.KINDS, kinds);

        Execution body = factory().newNode(Execution.class, getChildren(ompBlockConstruct, FlangName.EXECUTION_PART_CONSTRUCT));
        ompBlockConstruct.addChild(body);

        if (attributes().get(clauseList).has(FlangName.OMP_CLAUSE)) ompBlockConstruct.addChildren(getChildren(clauseList, FlangName.OMP_CLAUSE));
    }

    public void ompLoopConstruct(OmpLoopConstruct ompLoopConstruct) {
        String directive = attributes().getString(ompLoopConstruct, "directive", FlangName.OMP_BEGIN_LOOP_DIRECTIVE, FlangName.OMP_LOOP_DIRECTIVE);
        String clauseList = attributes().getString(ompLoopConstruct, "id", FlangName.OMP_BEGIN_LOOP_DIRECTIVE, FlangName.OMP_CLAUSE_LIST);

        List<OmpDirectiveKind> kinds = OmpDirectiveKind.getKinds(directive);

        ompLoopConstruct.set(OmpBlockConstruct.KINDS, kinds);

        DoStmt loop = (DoStmt) getChild(ompLoopConstruct, FlangName.DO_CONSTRUCT);
        ompLoopConstruct.addChild(loop);

        if (attributes().get(clauseList).has(FlangName.OMP_CLAUSE)) ompLoopConstruct.addChildren(getChildren(clauseList, FlangName.OMP_CLAUSE));
    }

    public void ompDataSharingClause(OmpDataSharingClause ompDataSharingClause) {
        ompDataSharingClause.addChildren(
                getChildren(attributes(ompDataSharingClause).getString(FlangName.OMP_OBJECT_LIST.getString()), FlangName.OMP_OBJECT)
        );

        String kind = attributes(ompDataSharingClause).getString("kind");

        ompDataSharingClause.set(OmpDataSharingClause.KIND, OmpClauseKind.valueOf(kind));
    }
}
