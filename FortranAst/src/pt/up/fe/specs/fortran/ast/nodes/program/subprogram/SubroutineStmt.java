package pt.up.fe.specs.fortran.ast.nodes.program.subprogram;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.decl.Parameter;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static pt.up.fe.specs.fortran.ast.FortranKeyword.SUBROUTINE;

public class SubroutineStmt extends Stmt {
    public static final DataKey<String> SUBROUTINE_NAME = KeyFactory.string("subroutine_name");

    public SubroutineStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getSubroutineName() {
        return get(SUBROUTINE_NAME);
    }

    public List<Parameter> getParameters() {
        return getChildrenOf(Parameter.class);
    }

    @Override
    public String getStmtCode() {
        var subroutineName = getSubroutineName();

        var argCode = getParameters().stream()
                .map(Parameter::getCode)
                .collect(Collectors.joining(", ", "(", ")"));

        return keyword(SUBROUTINE) + " " + subroutineName + argCode;
    }
}
