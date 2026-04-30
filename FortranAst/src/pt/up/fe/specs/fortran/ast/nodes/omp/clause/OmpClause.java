package pt.up.fe.specs.fortran.ast.nodes.omp.clause;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.omp.enums.OmpClauseKind;

import java.util.Collection;

public abstract class OmpClause extends FortranNode {

    public static final DataKey<OmpClauseKind> KIND = KeyFactory.enumeration("kind", OmpClauseKind.class);

    public OmpClause(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }
}
