package pt.up.fe.specs.fortran.ast.nodes.io;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ActionStmt;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class WaitStmt extends ActionStmt {
    public WaitStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<WaitSpec> getSpecs() {
        return getChildren(WaitSpec.class);
    }

    @Override
    public String getStmtCode() {
        var specsCode = getSpecs().stream()
                .map(WaitSpec::getCode)
                .collect(Collectors.joining(", ", "(", ")"));

        return keyword(FortranKeyword.WAIT) + specsCode;
    }
}
