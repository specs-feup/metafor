package pt.up.fe.specs.fortran.ast.nodes.omp.enums;

import pt.up.fe.specs.util.providers.StringProvider;

import java.util.Arrays;
import java.util.List;

public enum OmpDirectiveKind implements StringProvider {
    PARALLEL,
    DO;

    public static List<OmpDirectiveKind> getKinds(String directive) {
        return Arrays.stream(directive.split(" "))
                .map(OmpDirectiveKind::valueOf)
                .toList();
    }

    @Override
    public String getString() {
        return this.toString();
    }
}
