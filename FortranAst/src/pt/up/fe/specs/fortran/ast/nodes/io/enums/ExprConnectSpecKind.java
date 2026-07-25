package pt.up.fe.specs.fortran.ast.nodes.io.enums;

import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.providers.StringProvider;

public enum ExprConnectSpecKind implements StringProvider {
    UNIT,
    ACCESS,
    ACTION,
    ASYNCHRONOUS,
    BLANK,
    DECIMAL,
    DELIM,
    ENCODING,
    ERR,
    FILE,
    FORM,
    PAD,
    POSITION,
    RECL,
    ROUND,
    SIGN,
    STATUS,
    CARRIAGECONTROL,
    CONVERT,
    DISPOSE;

    private final String string;

    ExprConnectSpecKind() {
        string = SpecsStrings.toCamelCase(name());
    }

    public String getString() {
        return string;
    }
}
