package pt.up.fe.specs.fortran.ast.nodes.program;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.construct.DeclConstruct;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ImportStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.usestmt.UseStmt;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Specification extends FortranNode {
    public Specification(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<UseStmt> getUseStmts() {
        return getChildrenOf(UseStmt.class);
    }

    public List<ImportStmt> getImportStmts() {
        return getChildrenOf(ImportStmt.class);
    }

    public ImplicitPart getImplicitPart() {
        return getChild(ImplicitPart.class);
    }

    public List<DeclConstruct> getDeclarationConstructs() {
        return getChildrenOf(DeclConstruct.class);
    }

    public void addUseStmt(UseStmt stmt) {
        addChild(0, stmt);
    }

    @Override
    public String getCode() {
        var useStmtsCode = getUseStmts().stream()
                .map(UseStmt::getCode)
                .collect(Collectors.joining(ln()));

        var importStmtsCode = getImportStmts().stream()
                .map(ImportStmt::getCode)
                .collect(Collectors.joining(ln()));

        var implicitPartCode = getImplicitPart().getCode();

        var declConstructsCode = getDeclarationConstructs().stream()
                .map(DeclConstruct::getCode)
                .collect(Collectors.joining(ln()));

        return Stream.of(useStmtsCode, importStmtsCode, implicitPartCode, declConstructsCode)
                .filter(code -> !code.isEmpty())
                .collect(Collectors.joining(ln()));
    }
}
