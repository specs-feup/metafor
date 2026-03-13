package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.omp.OmpBlockConstruct;
import pt.up.fe.specs.fortran.ast.nodes.omp.enums.OmpDirectiveKind;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

import java.util.List;

public class OmpProcessors extends ANodeProcessor {
    public OmpProcessors(FortranJsonResult data) {
        super(data);
    }

    public void ompBlockConstruct(OmpBlockConstruct ompBlockConstruct) {
        String directive = attributes().getString(ompBlockConstruct, "directive", FlangName.OMP_BEGIN_BLOCK_DIRECTIVE, FlangName.OMP_BLOCK_DIRECTIVE);

        List<OmpDirectiveKind> kinds = OmpDirectiveKind.getKinds(directive);

        ompBlockConstruct.set(OmpBlockConstruct.KINDS, kinds);

        Execution body = factory().newNode(Execution.class, getChildren(ompBlockConstruct, FlangName.EXECUTION_PART_CONSTRUCT));
        ompBlockConstruct.addChild(body);
    }
}
