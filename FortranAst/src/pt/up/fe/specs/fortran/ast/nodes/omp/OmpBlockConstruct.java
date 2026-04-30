package pt.up.fe.specs.fortran.ast.nodes.omp;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.fortran.ast.FortranKeyword;
import pt.up.fe.specs.fortran.ast.nodes.FortranNode;
import pt.up.fe.specs.fortran.ast.nodes.omp.enums.OmpDirectiveKind;
import pt.up.fe.specs.fortran.ast.nodes.program.Execution;

import java.util.Collection;
import java.util.stream.Collectors;

public class OmpBlockConstruct extends OmpConstruct {
    public OmpBlockConstruct(DataStore data, Collection<? extends FortranNode> children) {
        super(data, children);
    }

    public Execution getBody() {
        return getChild(Execution.class);
    }

    @Override
    public String getCode() {
        var code = new StringBuilder();
        String directive = get(KINDS).stream()
                .map(OmpDirectiveKind::getString)
                .collect(Collectors.joining(" "));

        code.append("!$").append(FortranKeyword.OMP).append(" ").append(directive).append(" ").append(getClauseCode());

        code.append(ln()).append(getBody().getCode()).append(ln());

        code.append("!$").append(FortranKeyword.OMP).append(" ").append(FortranKeyword.END).append(" ").append(directive);

        return code.toString();
    }
}
