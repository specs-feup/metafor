package pt.up.fe.specs.fortran.ast.nodes.program;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class InternalSubprogram extends FortranNode {
    public InternalSubprogram(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<Subroutine> getSubprograms() {
        return getChildrenOf(Subroutine.class);
    }

    @Override
    public String getCode() {
        return getSubprograms().stream()
                .map(Subroutine::getCode)
                .collect(Collectors.joining(ln()));
    }
}
