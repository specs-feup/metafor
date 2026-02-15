package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.EntityDecl;
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
        return getChildren(EntityDecl.class);
    }

    @Override
    public String getCode() {
        //     integer :: a, b = 1;
        var code = new StringBuilder();

        var decls = getDecls();

        SpecsCheck.checkArgument(!decls.isEmpty(), () -> "TypeDeclarationStmt should have at least one EntityDecl");

        var type = decls.get(0).getType();

        code.append(type.getCode());
        code.append(" :: ");

        var declsCode = decls.stream().map(decl -> decl.getCode())
                .collect(Collectors.joining(", "));
        code.append(declsCode);

        return code.toString();
    }
}
