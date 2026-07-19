package pt.up.fe.specs.fortran.ast.nodes.program;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ExecutableConstruct;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class StmtBlock extends FortranNode {

    public StmtBlock(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    // TODO(Process-ing): Modify the weaver to avoid needing this wrapper
    public List<FortranNode> getStatements() {
        return getChildren();
    }

    @Override
    public String getCode() {
        return getStatements().stream()
                .map(FortranNode::getCode)
                .collect(Collectors.joining(ln()));
    }
}
