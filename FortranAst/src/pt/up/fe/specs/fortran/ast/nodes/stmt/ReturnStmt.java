package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.IntLiteral;

import java.util.Collection;
import java.util.Optional;

import static pt.up.fe.specs.fortran.ast.FortranKeyword.RETURN;

public class ReturnStmt extends ExecutableStmt {
    public ReturnStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<IntLiteral> getTarget() {
        return getChildOf(IntLiteral.class);
    }

    @Override
    public String getStmtCode() {
        var targetOpt = getTarget();
        var targetSuffix = targetOpt.map(target -> " " + target.getCode()).orElse("");

        return keyword(RETURN) + targetSuffix;
    }
}
