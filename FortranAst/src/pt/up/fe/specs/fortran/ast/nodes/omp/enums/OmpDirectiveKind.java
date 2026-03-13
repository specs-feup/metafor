package pt.up.fe.specs.fortran.ast.nodes.omp.enums;

import pt.up.fe.specs.util.providers.StringProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum OmpDirectiveKind implements StringProvider {
    PARALLEL,
    DO;

    public static List<OmpDirectiveKind> getKinds(String directive) {
        return Arrays.stream(directive.split(" "))
                .map(String::toUpperCase)
                .map(OmpDirectiveKind::valueOf)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public String getString() {
        return this.toString();
    }
}
