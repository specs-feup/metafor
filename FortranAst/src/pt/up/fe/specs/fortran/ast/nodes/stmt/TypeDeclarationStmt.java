package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.EntityDecl;
import pt.up.fe.specs.fortran.ast.nodes.specification.ArraySpecification;
import pt.up.fe.specs.fortran.ast.nodes.type.attributes.AttributeSpecifier;
import pt.up.fe.specs.util.SpecsCheck;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Has EntityDecl as children, each one representing an entity declaration.
 */
public class TypeDeclarationStmt extends SpecificationStmt {

    public TypeDeclarationStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }


    public List<EntityDecl> getDecls() {
        return getChildrenOf(EntityDecl.class);
    }

    public List<AttributeSpecifier> getAttributes() {
        return getChildrenOf(AttributeSpecifier.class);
    }

    @Override
    public String getStmtCode() {
        //     integer :: a, b = 1;
        var code = new StringBuilder();

        var decls = getDecls();
        var attributes = getAttributes();

        SpecsCheck.checkArgument(!decls.isEmpty(), () -> "TypeDeclarationStmt should have at least one EntityDecl");

        var type = decls.get(0).getType();

        code.append(type.getCode());
        if (!attributes.isEmpty()) {
            var attributesCode = getAttributesCode(attributes);
            code.append(",").append(optSpc()).append(attributesCode);
        }

        code.append(!fixedForm() ? " :: " : " ");

        var declsCode = decls.stream().map(EntityDecl::getCode)
                .collect(Collectors.joining("," + optSpc()));
        code.append(declsCode);

        return code.toString();
    }

    private static String getAttributesCode(List<AttributeSpecifier> attributes) {
        return attributes.stream().map(FortranNode::getCode)
                .collect(Collectors.joining(", "));
    }
}
