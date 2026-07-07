package pt.up.fe.specs.fortran.ast.nodes.program;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.DummyArgumentDecl;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static pt.up.fe.specs.fortran.ast.FortranKeyword.SUBROUTINE;

public class SubroutineStmt extends Stmt {
    public SubroutineStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<DummyArgumentDecl> getDummyArgs() {
        return getChildrenOf(DummyArgumentDecl.class);
    }

    @Override
    public String getStmtCode() {
        var subroutineName = getAncestor(Subroutine.class).getName();

        var argCode = getDummyArgs().stream()
                .map(DummyArgumentDecl::getCode)
                .collect(Collectors.joining(", ", "(", ")"));

        return keyword(SUBROUTINE) + " " + subroutineName + argCode;
    }
}
