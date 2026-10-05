package pt.up.fe.specs.fortran.ast.nodes.expr;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.type.DerivedType;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class StructureConstructor extends Expr {
    public StructureConstructor(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public DerivedType getStructureType() {
        return getChild(DerivedType.class, 0);
    }

    public List<ComponentSpec> getArguments() {
        return getChildrenOf(ComponentSpec.class);
    }

    @Override
    public String getCode() {
        var typeCode = getStructureType().getCode();
        var argsCode = getArguments().stream()
                .map(ComponentSpec::getCode)
                .collect(Collectors.joining(", ", "(", ")"));
        return typeCode + argsCode;
    }
}
