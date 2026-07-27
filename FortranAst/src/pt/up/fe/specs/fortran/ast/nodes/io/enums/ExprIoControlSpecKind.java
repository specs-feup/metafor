package pt.up.fe.specs.fortran.ast.nodes.io.enums;

public enum ExprIoControlSpecKind {
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

    public static ExprIoControlSpecKind convert(String name) {
        return valueOf(name.toUpperCase());
    }
}
