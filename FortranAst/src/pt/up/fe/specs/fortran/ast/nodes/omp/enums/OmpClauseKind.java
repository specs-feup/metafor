package pt.up.fe.specs.fortran.ast.nodes.omp.enums;

import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.providers.StringProvider;

public enum OmpClauseKind implements StringProvider {
    PRIVATE,
    SHARED,
    FIRST_PRIVATE;

    @Override
    public String getString() {
        return this.toString();
    }
}
