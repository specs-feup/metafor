package pt.up.fe.specs.fortran.ast.nodes.io.enums;

import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.providers.StringProvider;

public enum ExprIoControlSpecKind implements StringProvider {
    ASYNCHRONOUS,
    ADVANCE,
    BLANK,
    DECIMAL,
    DELIM,
    PAD,
    POS,
    REC,
    ROUND,
    SIGN,
    UNIT;

    private final String string;

    ExprIoControlSpecKind() {
        string = SpecsStrings.toCamelCase(name());
    }

    @Override
    public String getString() {
        return string;
    }
}
