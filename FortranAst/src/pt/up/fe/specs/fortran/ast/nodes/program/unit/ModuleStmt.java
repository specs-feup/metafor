package pt.up.fe.specs.fortran.ast.nodes.program.unit;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.stmt.Stmt;

import java.util.Collection;

public class ModuleStmt extends Stmt {
    public static final DataKey<String> MODULE_NAME = KeyFactory.string("module_name");

    public ModuleStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public String getModuleName() {
        return get(MODULE_NAME);
    }

    @Override
    public String getStmtCode() {
        return keyword(FortranKeyword.MODULE) + " " + getModuleName();
    }
}
