package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.stream.Collectors;

public class AcImpliedDo extends Expr {
    public AcImpliedDo(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var bodyExprs = getChildrenOf(Expr.class);
        var control = getChild(AcImpliedDoControl.class, 0);
        var bodyStr = bodyExprs.stream()
                .map(FortranNode::getCode)
                .collect(Collectors.joining(", "));
        return "(" + bodyStr + ", " + control.getCode() + ")";
    }
}
