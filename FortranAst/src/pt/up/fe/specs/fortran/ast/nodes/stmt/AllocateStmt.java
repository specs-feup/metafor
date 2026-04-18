package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.Allocation;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AllocateStmt extends ActionStmt {
    public AllocateStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<Allocation> getAllocations() {
        return getChildrenOf(Allocation.class);
    }

    @Override
    public String getCode() {
        StringBuilder code = new StringBuilder();

        code.append("allocate(");

        code.append(getAllocations()
                .stream()
                .map(Allocation::getCode)
                .collect(Collectors.joining(", "))
        );

        code.append(")");

        return code.toString();
    }
}
