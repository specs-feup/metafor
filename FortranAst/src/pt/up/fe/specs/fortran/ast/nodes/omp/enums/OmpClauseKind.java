package pt.up.fe.specs.fortran.ast.nodes.omp.enums;

import pt.up.fe.specs.util.providers.StringProvider;

public enum OmpClauseKind implements StringProvider {
    PRIVATE,
    SHARED,
    FIRSTPRIVATE;

    @Override
    public String getString() {
        return this.toString();
    }
}
