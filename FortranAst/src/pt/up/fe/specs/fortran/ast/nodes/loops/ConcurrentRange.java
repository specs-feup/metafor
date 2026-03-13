package pt.up.fe.specs.fortran.ast.nodes.loops;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class ConcurrentRange extends RangeLoopControl {

    public ConcurrentRange(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        StringBuilder code = new StringBuilder();

        code.append(getVar().getCode()).append(" = ")
                .append(getLower().getCode()).append(":")
                .append(getUpper().getCode());

        getStep().ifPresent(step -> code.append(":").append(step.getCode()));

        return code.toString();
    }
}
