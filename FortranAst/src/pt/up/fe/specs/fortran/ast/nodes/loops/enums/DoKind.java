package pt.up.fe.specs.fortran.ast.nodes.loops.enums;

import pt.up.fe.specs.fortran.ast.nodes.loops.ALoopControl;
import pt.up.fe.specs.fortran.ast.nodes.loops.LoopRange;
import pt.up.fe.specs.fortran.ast.nodes.loops.WhileLoopControl;
import pt.up.fe.specs.util.providers.StringProvider;


public enum DoKind implements StringProvider {
    Range,
    While;

    public static DoKind getKindFromControl(ALoopControl control) {
        if (control instanceof LoopRange) {
            return Range;
        }
        else if (control instanceof WhileLoopControl) {
            return While;
        }

        return null;
    }

    @Override
    public String getString() {
        return this.toString();
    }
}
