package pt.up.fe.specs.fortran.ast.nodes.decl;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Collection;

/**
 * Represents the declaration of a label, that appears before a statement.
 */
// TODO(Process-ing): Replace this label for a standard label in ExecutableStmt
public class LabelDecl extends FortranDecl {

    // DATAKEYS BEGIN

    /**
     * An integer, from 1 to 99999, representing the possible label of the statement.
     */
    public final static DataKey<Integer> LABEL = KeyFactory.integer("label");

    // DATAKEYS END

    public LabelDecl(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Integer getValue() {
        return get(LABEL);
    }

    @Override
    public String getCode() {
        return get(LABEL).toString();
    }
}
