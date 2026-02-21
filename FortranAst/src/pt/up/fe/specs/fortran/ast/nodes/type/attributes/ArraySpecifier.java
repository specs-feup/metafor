package pt.up.fe.specs.fortran.ast.nodes.type.attributes;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.TypeDeclarationStmt;
import pt.up.fe.specs.fortran.ast.nodes.type.shapes.ShapeSpecification;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ArraySpecifier extends AttributeSpecifier {
    public ArraySpecifier(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    private List<ShapeSpecification> getShapes() {
        return getChildren(ShapeSpecification.class);
    }

    private boolean isTypeDeclarationParen() {
        return getParent() instanceof TypeDeclarationStmt;
    }

    @Override
    public String getCode() {
        var shapes = getShapes();
        var code = new StringBuilder();

        if (isTypeDeclarationParen()) {
            // That means that the array specifier is part of a type attributed as dimension(5) etc.
            code.append("dimension");
        }

        code.append("(");
        var shapesCode = shapes.stream().map(ShapeSpecification::getCode).collect(Collectors.joining(", "));
        code.append(shapesCode);
        code.append(")");
        return code.toString();
    }
}
