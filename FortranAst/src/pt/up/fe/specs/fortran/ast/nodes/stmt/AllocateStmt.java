package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.alloc.AllocOption;
import pt.up.fe.specs.fortran.ast.nodes.alloc.Allocation;

import java.util.ArrayList;
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

    public List<AllocOption> getOptions() {
        return getChildrenOf(AllocOption.class);
    }

    @Override
    public String getStmtCode() {
        StringBuilder code = new StringBuilder();

        code.append("allocate(");

        List<String> components = new ArrayList<>();

        getAllocations().stream()
                .map(FortranNode::getCode)
                .forEach(components::add);

        getOptions().stream()
                .map(FortranNode::getCode)
                .forEach(components::add);

        code.append(String.join(", ", components));

        code.append(")");

        return code.toString();
    }
}
