package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.type.IntrinsicType;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AcSpecification extends FortranNode {
    public AcSpecification(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var typeStr = getType()
                .map(type -> type.getCode() + " ::")
                .orElse("");

        var acValuesStr = getAcValues().stream()
                .map(FortranNode::getCode)
                .collect(Collectors.joining(", "));

        return Stream.of(typeStr, acValuesStr)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(" "));
    }

    private Optional<IntrinsicType> getType() {
        return getChildTry(IntrinsicType.class);
    }


    private List<Expr> getAcValues() {
        return getChildrenOf(Expr.class);
    }
}
