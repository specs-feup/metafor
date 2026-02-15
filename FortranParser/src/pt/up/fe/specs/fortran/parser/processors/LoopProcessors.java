package pt.up.fe.specs.fortran.parser.processors;

import pt.up.fe.specs.fortran.ast.nodes.loops.LoopBounds;
import pt.up.fe.specs.fortran.ast.nodes.loops.LoopControl;
import pt.up.fe.specs.fortran.ast.nodes.loops.enums.DoKind;
import pt.up.fe.specs.fortran.ast.nodes.variable.DataRef;
import pt.up.fe.specs.fortran.parser.FlangName;
import pt.up.fe.specs.fortran.parser.FortranJsonResult;

public class LoopProcessors extends ANodeProcessor {
    public LoopProcessors(FortranJsonResult data) {
        super(data);
    }

    public void loopControl(LoopControl loopControl) {
        DoKind kind = DoKind.valueOf(attributes().getString(loopControl, "kind"));

        loopControl.set(LoopControl.KIND, kind);

        loopControl.addChild(getChild(loopControl, "value"));
    }

    public void loopBounds(LoopBounds loopBounds) {
        String varName = attributes().getAttrs(attributes().getString(loopBounds, "var")).getString("source");
        DataRef varRef = factory().newNode(DataRef.class);
        varRef.set(DataRef.NAME, varName);
        loopBounds.addChild(varRef);

        loopBounds.addChild(getChild(loopBounds, "lower"));
        loopBounds.addChild(getChild(loopBounds, "upper"));

        attributes().getOptionalString(loopBounds, "step").ifPresent(
                s -> loopBounds.addChild(getChild(loopBounds, "step"))
        );
    }
}
