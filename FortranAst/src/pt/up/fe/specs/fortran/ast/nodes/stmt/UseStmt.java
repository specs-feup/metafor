package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

public class UseStmt extends Stmt {

    public static DataKey<String> NAME = KeyFactory.string("name");

    public UseStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getName() {
        return get(NAME);
    }

    @Override
    public String getStmtCode() {
        return FortranKeyword.USE + " " + getName();
    }
}
