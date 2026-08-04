package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class GotoStmt extends ActionStmt {
    public static final DataKey<Integer> LABEL = KeyFactory.integer("label");

    public GotoStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public int getGotoLabel() {
        return get(LABEL);
    }

    @Override
    public String getStmtCode() {
        var label = getGotoLabel();

        return keyword(FortranKeyword.GO) + " " + keyword(FortranKeyword.TO) + " " + label;
    }
}
