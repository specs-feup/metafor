package pt.up.fe.specs.fortran.ast.nodes.io;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ActionStmt;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CloseStmt extends ActionStmt {
    public CloseStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<CloseSpec> getSpecs() {
        return getChildren(CloseSpec.class);
    }

    @Override
    public String getStmtCode() {
        var specsCode = getSpecs().stream()
                .map(CloseSpec::getCode)
                .collect(Collectors.joining(", ", "(", ")"));

        return keyword(FortranKeyword.CLOSE) + specsCode;
    }
}
