package pt.up.fe.specs.fortran.ast.nodes.omp;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.clause.OmpNowaitClause;
import pt.up.fe.specs.fortran.ast.nodes.omp.enums.OmpClauseKind;
import pt.up.fe.specs.fortran.ast.nodes.omp.enums.OmpDirectiveKind;
import pt.up.fe.specs.fortran.ast.nodes.stmt.loop.DoConstruct;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class OmpLoopConstruct extends OmpConstruct {
    public OmpLoopConstruct(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public DoConstruct getLoop() {
        return getChild(DoConstruct.class);
    }

    public DoConstruct setLoop(DoConstruct loop) {
        removeChildren(DoConstruct.class);

        return (DoConstruct) addChild(loop);
    }

    // For code generation purposes, since the nowait clause must be placed at the end
    private List<OmpClause> getFilteredClauses() {
        return getClauses()
                .stream()
                .filter(clause -> !(clause instanceof OmpNowaitClause))
                .toList();
    }

    public boolean hasNowait() {
        return getClauses()
                .stream()
                .anyMatch(clause -> clause instanceof OmpNowaitClause);
    }

    @Override
    public String getStmtCode() {
        var code = new StringBuilder();
        String directive = get(KINDS).stream()
                .map(OmpDirectiveKind::getString)
                .collect(Collectors.joining(" "));

        code.append("!$").append(FortranKeyword.OMP).append(" ").append(directive).append(" ").append(getClauseCode(getFilteredClauses()));

        code.append(ln()).append(getLoop().getCode()).append(ln());

        code.append("!$").append(FortranKeyword.OMP).append(" ").append(FortranKeyword.END).append(" ").append(directive);

        if (hasNowait()) code.append(" ").append(OmpClauseKind.NO_WAIT.getCode());

        return code.toString();
    }
}
