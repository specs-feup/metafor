package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.loops.ConcurrentLoopControl;
import pt.up.fe.specs.fortran.ast.nodes.loops.ConcurrentRange;
import pt.up.fe.specs.fortran.ast.nodes.loops.RangeLoopControl;
import pt.up.fe.specs.fortran.ast.nodes.expr.DataRef;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

import java.util.Optional;

public class LoopProcessors extends ANodeProcessor {
    public LoopProcessors(FortranJsonResult data) {
        super(data);
    }

    public void loopRange(RangeLoopControl rangeLoopControl) {
        String varName = attributes().getAttrs(attributes().getString(rangeLoopControl, "var")).getString("source");
        DataRef varRef = factory().newNode(DataRef.class);
        varRef.set(DataRef.NAME, varName);
        rangeLoopControl.addChild(varRef);

        rangeLoopControl.addChild(getChild(rangeLoopControl, "lower"));
        rangeLoopControl.addChild(getChild(rangeLoopControl, "upper"));

        attributes().getOptionalString(rangeLoopControl, "step").ifPresent(
                s -> rangeLoopControl.addChild(getChild(rangeLoopControl, "step"))
        );
    }

    public void concurrentRange(ConcurrentRange concurrentRange) {
        loopRange(concurrentRange);
    }

    public void concurrentLoopControl(ConcurrentLoopControl concurrentLoopControl) {
        String header = attributes().getString(concurrentLoopControl, "id", FlangName.CONCURRENT_HEADER);

        concurrentLoopControl.addChildren(getChildren(header, FlangName.CONCURRENT_CONTROL));

        attributes().getAttrs(header).getOptionalString("value").ifPresent(
                mask -> concurrentLoopControl.addChild(getChild(mask))
        );

    }
}
