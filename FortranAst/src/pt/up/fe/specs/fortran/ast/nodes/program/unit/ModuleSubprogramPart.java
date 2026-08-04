package pt.up.fe.specs.fortran.ast.nodes.program.unit;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.program.subprogram.Subprogram;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ContainsStmt;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleSubprogramPart extends FortranNode {
    public ModuleSubprogramPart(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public ContainsStmt getContainsStmt() {
        return getChild(ContainsStmt.class, 0);
    }

    public List<Subprogram> getSubprograms() {
        return getChildrenOf(Subprogram.class);
    }

    @Override
    public String getCode() {
        var containsStmtCode = getContainsStmt().getCode();
        var subprogramsCode = getSubprograms().stream()
                .map(subprogram -> ln() + ln() + indent(subprogram.getCode()))
                .collect(Collectors.joining());

        return containsStmtCode + subprogramsCode;
    }
}
