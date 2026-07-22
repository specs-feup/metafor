package pt.up.fe.specs.fortran.ast.nodes.stmt.dimstmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DimensionStmt extends Stmt {
    public DimensionStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<DimensionDecl> getDimensionDecls() {
        return getChildrenOf(DimensionDecl.class);
    }

    @Override
    public String getStmtCode() {
        var code = new StringBuilder();

        code.append(keyword(FortranKeyword.DIMENSION))
                .append(!fixedForm() ? " :: " : " ");

        var decls = getDimensionDecls();
        var declsCode = decls.stream()
                .map(DimensionDecl::getCode)
                .collect(Collectors.joining("," + optSpc()));
        code.append(declsCode);

        return code.toString();
    }
}
