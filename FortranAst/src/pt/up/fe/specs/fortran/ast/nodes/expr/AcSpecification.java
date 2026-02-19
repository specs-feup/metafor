package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.stream.Collectors;

public class AcSpecification extends FortranNode {
    public AcSpecification(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var acValues = getChildren(Expr.class, 0);

        return acValues.stream().map(FortranNode::getCode)
                .collect(Collectors.joining(", "));
    }
}
