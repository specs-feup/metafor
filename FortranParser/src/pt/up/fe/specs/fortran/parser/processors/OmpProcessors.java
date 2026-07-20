package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.expr.enums.BinaryOperatorKind;
import pt.up.fe.specs.fortran.ast.nodes.omp.OmpBlockConstruct;
import pt.up.fe.specs.fortran.ast.nodes.omp.OmpLoopConstruct;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpDataSharingClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpNowaitClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpReductionClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.enums.OmpClauseKind;
import pt.up.fe.specs.fortran.ast.nodes.omp.enums.OmpDirectiveKind;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;
import pt.up.fe.specs.fortran.ast.nodes.stmt.loop.DoConstruct;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

import java.util.List;

public class OmpProcessors extends ANodeProcessor {
    private StmtProcessors stmtProcessors;

    public OmpProcessors(FortranJsonResult data, StmtProcessors stmtProcessors) {
        super(data);

        this.stmtProcessors = stmtProcessors;
    }

    public void ompBlockConstruct(OmpBlockConstruct ompBlockConstruct) {
        String directive = attributes().getString(ompBlockConstruct, "directive", FlangName.OMP_BEGIN_DIRECTIVE, FlangName.OMP_DIRECTIVE_NAME);
        String clauseList = attributes().getString(ompBlockConstruct, "id", FlangName.OMP_BEGIN_DIRECTIVE, FlangName.OMP_CLAUSE_LIST);

        List<OmpDirectiveKind> kinds = OmpDirectiveKind.getKinds(directive);

        ompBlockConstruct.set(OmpBlockConstruct.KINDS, kinds);

        Execution body = factory().newNode(Execution.class, getChildren(ompBlockConstruct, FlangName.EXECUTION_PART_CONSTRUCT));
        ompBlockConstruct.addChild(body);

        if (attributes().get(clauseList).has(FlangName.OMP_CLAUSE))
            ompBlockConstruct.addChildren(getChildren(clauseList, FlangName.OMP_CLAUSE));
    }

    public void ompLoopConstruct(OmpLoopConstruct ompLoopConstruct) {
        String directive = attributes().getString(ompLoopConstruct, "directive", FlangName.OMP_BEGIN_LOOP_DIRECTIVE, FlangName.OMP_DIRECTIVE_NAME);
        String clauseList = attributes().getString(ompLoopConstruct, "id", FlangName.OMP_BEGIN_LOOP_DIRECTIVE, FlangName.OMP_CLAUSE_LIST);
        String endClauseList = attributes().getString(ompLoopConstruct, "id", FlangName.OMP_END_LOOP_DIRECTIVE, FlangName.OMP_CLAUSE_LIST);

        List<OmpDirectiveKind> kinds = OmpDirectiveKind.getKinds(directive);

        ompLoopConstruct.set(OmpBlockConstruct.KINDS, kinds);

        DoConstruct loop = (DoConstruct) getChildren(ompLoopConstruct, FlangName.EXECUTION_PART_CONSTRUCT).get(0);
        ompLoopConstruct.addChild(loop);

        if (attributes().get(clauseList).has(FlangName.OMP_CLAUSE))
            ompLoopConstruct.addChildren(getChildren(clauseList, FlangName.OMP_CLAUSE));
        if (attributes().get(endClauseList).has(FlangName.OMP_CLAUSE))
            ompLoopConstruct.addChildren(getChildren(endClauseList, FlangName.OMP_CLAUSE));
    }

    public void ompDataSharingClause(OmpDataSharingClause ompDataSharingClause) {
        ompDataSharingClause.addChildren(
                getChildren(attributes(ompDataSharingClause).getString(FlangName.OMP_OBJECT_LIST.getString()), FlangName.OMP_OBJECT)
        );

        String kind = attributes(ompDataSharingClause).getString("kind");

        ompDataSharingClause.set(OmpDataSharingClause.KIND, OmpClauseKind.valueOf(kind));
    }

    public void ompReductionClause(OmpReductionClause ompReductionClause) {
        ompReductionClause.addChildren(
                getChildren(attributes().getString(ompReductionClause, FlangName.OMP_OBJECT_LIST.getString(), FlangName.OMP_REDUCTION_CLAUSE), FlangName.OMP_OBJECT)
        );

        List<String> modifiers = attributes().get(
                attributes(ompReductionClause).getString(FlangName.OMP_REDUCTION_CLAUSE)
        ).getStringList(FlangName.MODIFIER);

        String identifier = attributes().get(modifiers.get(0)).getVariantString();
        String operatorId = attributes().get(identifier).getVariantString();
        String operator = attributes().get(operatorId).getString("op");

        ompReductionClause.set(OmpReductionClause.OPERATOR, BinaryOperatorKind.valueOf(operator.toUpperCase()));
        ompReductionClause.set(OmpReductionClause.KIND, OmpClauseKind.REDUCTION);
    }

    public void ompNowaitClause(OmpNowaitClause ompNowaitClause) {
        ompNowaitClause.set(OmpNowaitClause.KIND, OmpClauseKind.NO_WAIT);
    }
}
