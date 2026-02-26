package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.type.IntrinsicType;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AcSpecification extends FortranNode {
    public AcSpecification(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {

        var code = new StringBuilder();

        var acValues = getAcValues();
        var typeTry = getType();
        typeTry.ifPresent(type -> code.append(type.getCode()).append(" ::"));

        if (!acValues.isEmpty() && typeTry.isPresent()) {
            code.append(" ");
        }

        code.append(acValues.stream().map(FortranNode::getCode)
                .collect(Collectors.joining(", ")));

        return code.toString();
    }

    private Optional<IntrinsicType> getType() {
        return getChildTry(IntrinsicType.class);
    }


    private List<Expr> getAcValues() {
        return getChildrenOf(Expr.class);
    }
}
