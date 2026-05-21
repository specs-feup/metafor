package pt.up.fe.specs.fortran.ast.nodes.stmt.stopstmt;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ActionStmt;

import java.util.Collection;
import java.util.Optional;

public class StopStmt extends ActionStmt {
    public static final DataKey<Boolean> ERROR_STOP = KeyFactory.bool("errorStop");

    public StopStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public boolean isErrorStop() {
        return get(ERROR_STOP);
    }

    public Optional<StopCode> getStopCode() {
        return getChildOf(StopCode.class);
    }

    public Optional<QuietValue> getQuiet() {
        return getChildOf(QuietValue.class);
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
