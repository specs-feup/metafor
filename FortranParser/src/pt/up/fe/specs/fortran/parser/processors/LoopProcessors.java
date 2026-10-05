package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.expr.dataref.NameDataRef;
import pt.up.fe.specs.fortran.ast.nodes.loops.ConcurrentLoopControl;
import pt.up.fe.specs.fortran.ast.nodes.loops.ConcurrentRange;
import pt.up.fe.specs.fortran.ast.nodes.loops.RangeLoopControl;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

public class LoopProcessors extends ANodeProcessor {
    public LoopProcessors(FortranJsonResult data) {
        super(data);
    }

    public void loopRange(RangeLoopControl rangeLoopControl) {
        var variableId = attributes().getString(rangeLoopControl, "var");
        var variable = attributes().getAttrs(variableId).getString("source");
        rangeLoopControl.set(RangeLoopControl.VARIABLE, variable);

        var lower = getChild(rangeLoopControl, "lower");
        rangeLoopControl.addChild(lower);

        var upper = getChild(rangeLoopControl, "upper");
        rangeLoopControl.addChild(upper);

        var step = getChildOptional(rangeLoopControl, "step");
        step.ifPresent(rangeLoopControl::addChild);
    }

    public void concurrentRange(ConcurrentRange concurrentRange) {
        loopRange(concurrentRange);
    }

    public void concurrentLoopControl(ConcurrentLoopControl concurrentLoopControl) {
        String header = attributes().getString(concurrentLoopControl, "id", FlangName.CONCURRENT_HEADER);

        concurrentLoopControl.addChildren(getChildren(header, FlangName.CONCURRENT_CONTROL));

        attributes().getAttrs(header).getOptionalString(FlangName.EXPR.getString()).ifPresent(
                mask -> concurrentLoopControl.addChild(getChild(mask))
        );

    }
}
