package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.loops.RangeLoopControl;

import java.util.Collection;

public class AcImpliedDoControl extends FortranNode {
    public AcImpliedDoControl(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var loopControl = getRangeLoopControll();
        return loopControl.getCode();
    }


    private RangeLoopControl getRangeLoopControll() {
        return getChild(RangeLoopControl.class, 0);
    }
}
