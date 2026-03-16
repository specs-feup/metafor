package pt.up.fe.specs.fortran.ast.nodes.omp;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.enums.OmpDirectiveKind;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ExecutableStmt;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

abstract public class OmpConstruct extends ExecutableStmt {

    public final static DataKey<List<OmpDirectiveKind>> KINDS = KeyFactory.enumerationMulti("kinds", OmpDirectiveKind.class);

    public OmpConstruct(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<OmpClause> getClauses() {
        return getChildrenOf(OmpClause.class);
    }

    public String getClauseCode() {
        return getClauses().stream()
            .map(OmpClause::getCode)
            .collect(Collectors.joining(" "));
    }
}
