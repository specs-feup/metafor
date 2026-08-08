package pt.up.fe.specs.fortran.ast.nodes.omp;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.enums.OmpDirectiveKind;
import pt.up.fe.specs.fortran.ast.nodes.stmt.ExecutableConstruct;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

abstract public class OmpConstruct extends ExecutableConstruct {

    public final static DataKey<List<OmpDirectiveKind>> KINDS = KeyFactory.enumerationMulti("kinds", OmpDirectiveKind.class);

    public OmpConstruct(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public List<OmpClause> getClauses() {
        return getChildrenOf(OmpClause.class);
    }

    public void setClauses(List<OmpClause> clauses) {
        removeChildren(OmpClause.class);

        addChildren(clauses);
    }

    protected static String getClauseCode(List<OmpClause> clauses) {
        return clauses.stream()
                .map(OmpClause::getCode)
                .collect(Collectors.joining(" "));
    }

    public String getClauseCode() {
        return getClauseCode(getClauses());
    }
}
