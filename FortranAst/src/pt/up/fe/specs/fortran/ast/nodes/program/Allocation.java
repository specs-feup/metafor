package pt.up.fe.specs.fortran.ast.nodes.program;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.DataRef;
import pt.up.fe.specs.fortran.ast.nodes.type.shapes.AllocateShapeSpecification;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Allocation extends FortranNode {
    public Allocation(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public DataRef getRef() {
        return getChild(DataRef.class);
    }

    public List<AllocateShapeSpecification> getShapes() {
        return getChildrenOf(AllocateShapeSpecification.class);
    }

    @Override
    public String getCode() {
        StringBuilder code = new StringBuilder();

        code.append(getRef().getCode()).append("(");

        code.append(getShapes()
                .stream()
                .map(AllocateShapeSpecification::getCode)
                .collect(Collectors.joining(", "))
        );

        code.append(")");

        return code.toString();
    }
}
