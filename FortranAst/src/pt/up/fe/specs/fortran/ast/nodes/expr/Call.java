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

    public ProcDesignator getCallee() {
        return getChild(ProcDesignator.class, 0);
    }

    public List<ArgumentSpec> getArgs() {
        return getChildren(ArgumentSpec.class, 1);
    }

    @Override
    public String getCode() {
        var calleeCode = getCallee().getCode();
        var argsCode = getArgs().stream()
                .map(ArgumentSpec::getCode)
                .collect(Collectors.joining(", ", "(", ")"));

        return calleeCode + argsCode;
    }
}
