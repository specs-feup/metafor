package pt.up.fe.specs.fortran.ast.nodes.program;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ContainsStmt;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InternalSubprogram extends FortranNode {
    public InternalSubprogram(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<ContainsStmt> getContains() {
        return getChildTry(ContainsStmt.class);
    }

    public List<Subroutine> getSubprograms() {
        return getChildrenOf(Subroutine.class);
    }

    @Override
    public String getCode() {
        StringBuilder code = new StringBuilder();

        getContains().ifPresent(stmt -> code.append(stmt.getCode()).append(ln()));

        code.append(getSubprograms().stream()
                .map(Subroutine::getCode)
                .collect(Collectors.joining(ln())));

        return code.toString();
    }
}
