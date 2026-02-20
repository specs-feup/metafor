package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.loops.LoopBounds;
import pt.up.fe.specs.fortran.ast.nodes.loops.LoopControl;
import pt.up.fe.specs.fortran.ast.nodes.loops.LoopRange;
import pt.up.fe.specs.fortran.ast.nodes.loops.enums.DoKind;
import pt.up.fe.specs.fortran.ast.nodes.expr.DataRef;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

public class LoopProcessors extends ANodeProcessor {
    public LoopProcessors(FortranJsonResult data) {
        super(data);
    }

    public void loopRange(LoopRange loopRange) {
        String varName = attributes().getAttrs(attributes().getString(loopRange, "var")).getString("source");
        DataRef varRef = factory().newNode(DataRef.class);
        varRef.set(DataRef.NAME, varName);
        loopRange.addChild(varRef);

        loopRange.addChild(getChild(loopRange, "lower"));
        loopRange.addChild(getChild(loopRange, "upper"));

        attributes().getOptionalString(loopRange, "step").ifPresent(
                s -> loopRange.addChild(getChild(loopRange, "step"))
        );
    }
}
