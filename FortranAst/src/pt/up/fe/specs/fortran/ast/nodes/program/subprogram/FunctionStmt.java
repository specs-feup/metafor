package pt.up.fe.specs.fortran.ast.nodes.program.subprogram;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.NamedParameter;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;
import java.util.List;

// TODO(Process-ing): Include support for `prefix` and `suffix`
public class FunctionStmt extends Stmt {
    public FunctionStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<NamedParameter> getParameters() {
        return getChildrenOf(NamedParameter.class);
    }

    @Override
    public String getStmtCode() {
        var functionName = getAncestor(Function.class).getName();

        var argCode = getParameters().stream()
                .map(NamedParameter::getCode)
                .collect(java.util.stream.Collectors.joining(", ", "(", ")"));

        return keyword(FortranKeyword.FUNCTION) + " " + functionName + argCode;
    }
}
