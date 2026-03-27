package pt.up.fe.specs.fortran.ast.nodes.utils.enums;

import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.providers.StringProvider;

public enum IntentKind implements StringProvider {
    IN,
    OUT,
    INOUT;

    private final String string;

    IntentKind() {
        this.string = SpecsStrings.toCamelCase(name());
    }

    @Override
    public String getString() {
        return this.string;
    }
}
