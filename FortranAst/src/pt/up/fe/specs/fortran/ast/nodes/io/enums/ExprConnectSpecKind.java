package pt.up.fe.specs.fortran.ast.nodes.io.enums;

import pt.up.fe.specs.util.providers.StringProvider;

public enum ExprConnectSpecKind {
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

    public static ExprConnectSpecKind convert(String name) {
        return valueOf(name.toUpperCase());
    }
}
