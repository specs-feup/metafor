package pt.up.fe.specs.fortran.ast.nodes.omp.enums;

import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.providers.StringProvider;

public enum OmpClauseKind implements StringProvider {
    PRIVATE("private"),
    SHARED("shared"),
    FIRST_PRIVATE("firstprivate"),
    REDUCTION("reduction"),
    NO_WAIT("nowait"),
    ORDERED("ordered");

    private final String code;

    OmpClauseKind(String code) {
        this.code = code;
    }

    @Override
    public String getString() {
        return this.toString();
    }

    public String getCode() {
        return code;
    }
}
