package pt.up.fe.specs.fortran.weaver.joinpoints;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.loops.LoopControl;
import pt.up.fe.specs.fortran.weaver.abstracts.joinpoints.ALoopControl;

public class FLoopControl extends ALoopControl {

    private final LoopControl loopControl;

    public FLoopControl(LoopControl loopControl) {
        this.loopControl = loopControl;
    }

    @Override
    public FortranNode getNode() {
        return loopControl;
    }
}
