package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.expr.Expr;

import java.util.Collection;
import java.util.Optional;

public class StopStmt extends ActionStmt {
    public static final DataKey<Boolean> ERROR_STOP = KeyFactory.bool("errorStop");
    public static final DataKey<Boolean> HAS_CODE = KeyFactory.bool("hasCode");

    public StopStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public boolean isErrorStop() {
        return get(ERROR_STOP);
    }

    public Optional<Expr> getStopCode() {
        return get(HAS_CODE)
                ? Optional.of(getChild(Expr.class, 0))
                : Optional.empty();
    }

    public Optional<Expr> getQuiet() {
        var hasCode = get(HAS_CODE);
        var quietIdx = hasCode ? 1 : 0;
        var hasQuiet = quietIdx + 1 == getNumChildren();

        return hasQuiet
                ? Optional.of(getChild(Expr.class, quietIdx))
                : Optional.empty();
    }

    @Override
    public String getCode() {
        var errorStop = isErrorStop();
        var stopCodeOpt = getStopCode();
        var quietOpt = getQuiet();

        var code = new StringBuilder();

        if (errorStop) {
            code.append(keyword(FortranKeyword.ERROR)).append(" ");
        }
        code.append(keyword(FortranKeyword.STOP));

        stopCodeOpt.ifPresent(stopCode -> code.append(" ").append(stopCode.getCode()));

        quietOpt.ifPresent(quiet -> code.append(", ")
                .append(keyword(FortranKeyword.STOP))
                .append(" = ")
                .append(quiet.getCode()));

        return code.toString();
    }
}
