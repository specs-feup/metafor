package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;
import java.util.Optional;

import static pt.up.fe.specs.fortran.ast.FortranKeyword.RETURN;

public class ReturnStmt extends ActionStmt {
    public static final DataKey<Optional<Integer>> TARGET = KeyFactory.optional("target");

    public ReturnStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Optional<Integer> getTarget() {
        return get(TARGET);
    }

    @Override
    public String getStmtCode() {
        var targetOpt = getTarget();
        var targetSuffix = targetOpt.map(target -> " " + target).orElse("");

        return keyword(RETURN) + targetSuffix;
    }
}
