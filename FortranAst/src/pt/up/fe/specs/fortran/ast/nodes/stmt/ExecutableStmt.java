package pt.up.fe.specs.fortran.ast.nodes.stmt;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

/**
 * R514 executable-construct
 */
public abstract class ExecutableStmt extends Stmt {

    // DATAKEYS BEGIN

    /**
     * The original source of this statement.
     */
    public final static DataKey<String> SOURCE = KeyFactory.string("source");


    // DATAKEYS END

    public ExecutableStmt(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
