package pt.up.fe.specs.fortran.ast.nodes.program;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ImplicitPartStmt;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ImplicitPart extends FortranNode {
    public ImplicitPart(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<ImplicitPartStmt> getStatements() {
        return getChildren(ImplicitPartStmt.class);
    }

    @Override
    public String getCode() {
        return getStatements().stream()
                .map(ImplicitPartStmt::getCode)
                .collect(Collectors.joining(ln()));
    }
}
