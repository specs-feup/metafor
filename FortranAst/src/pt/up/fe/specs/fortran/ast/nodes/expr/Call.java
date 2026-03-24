package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Call extends Expr {

    public Call(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public DataRef getCallee() {
        return getChild(DataRef.class, 0);
    }

    public List<Argument> getArgs() {
        return getChildren(Argument.class, 1);
    }

    @Override
    public String getCode() {
        return getCallee().getCode() + "(" +
                getArgs().stream()
                        .map(Argument::getCode)
                        .collect(Collectors.joining(", ")) +
                ")";
    }
}
