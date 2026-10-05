package pt.up.fe.specs.fortran.ast.nodes.loops;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

// TODO(Process-ing): Refactor this node to not extend RangeLoopControl
public class ConcurrentRange extends RangeLoopControl {

    public ConcurrentRange(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        StringBuilder code = new StringBuilder();

        code.append(getVariable()).append(" = ")
                .append(getLower().getCode()).append(":")
                .append(getUpper().getCode());

        getStep().ifPresent(step -> code.append(":").append(step.getCode()));

        return code.toString();
    }
}
