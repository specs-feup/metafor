package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class NamelistStmt extends Stmt {
    public NamelistStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<NamelistGroup> getGroups() {
        return getChildren(NamelistGroup.class);
    }

    @Override
    public String getStmtCode() {
        var groupsCode = getGroups().stream()
                .map(NamelistGroup::getCode)
                .collect(Collectors.joining(", "));

        return keyword(FortranKeyword.NAMELIST) + " " + groupsCode;
    }
}
