package pt.up.fe.specs.fortran.ast.nodes.program;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.subprogram.InternalSubprogram;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ContainsStmt;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class InternalSubprogramPart extends FortranNode {
    public InternalSubprogramPart(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public ContainsStmt getContainsStmt() {
        return getChild(ContainsStmt.class);
    }

    public List<InternalSubprogram> getSubprograms() {
        return getChildrenOf(InternalSubprogram.class);
    }

    @Override
    public String getCode() {
        var containsStmtCode = getContainsStmt().getCode();
        var subprogramsCode = getSubprograms().stream()
                .map(subprogram -> ln() + subprogram.getCode())
                .collect(Collectors.joining());

        return containsStmtCode + subprogramsCode;
    }
}
