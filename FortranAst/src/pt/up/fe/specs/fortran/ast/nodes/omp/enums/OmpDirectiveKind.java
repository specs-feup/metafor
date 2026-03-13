package pt.up.fe.specs.fortran.ast.nodes.omp.enums;

import pt.up.fe.specs.util.providers.StringProvider;

public enum OmpDirectiveKind implements StringProvider {
    PARALLEL,
    DO;

    @Override
    public String getString() {
        return this.toString();
    }
}
