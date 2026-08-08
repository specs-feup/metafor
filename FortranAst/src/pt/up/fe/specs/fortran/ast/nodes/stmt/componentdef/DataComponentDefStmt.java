package pt.up.fe.specs.fortran.ast.nodes.stmt.componentdef;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.component.ComponentDecl;
import pt.up.fe.specs.fortran.ast.nodes.decl.component.attr.ComponentAttr;
import pt.up.fe.specs.fortran.ast.nodes.type.decltype.DeclType;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DataComponentDefStmt extends ComponentDefStmt {
    public DataComponentDefStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public DeclType getDeclType() {
        return getChild(DeclType.class, 0);
    }

    public List<ComponentAttr> getAttrs() {
        return getChildrenOf(ComponentAttr.class);
    }

    public List<ComponentDecl> getDecls() {
        return getChildrenOf(ComponentDecl.class);
    }

    @Override
    public String getStmtCode() {
        var declTypeCode = getDeclType().getCode();

        var attrsCode = getAttrs().stream()
                .map(attr -> ", " + attr.getCode())
                .collect(Collectors.joining());

        var declsCode = getDecls().stream()
                .map(ComponentDecl::getCode)
                .collect(Collectors.joining(", "));

        return declTypeCode + attrsCode + " :: " + declsCode;
    }
}
