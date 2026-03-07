package pt.up.fe.specs.fortran.ast;

public enum FortranKeyword {
    END,
    PROGRAM,
    PRINT,

    // Conditional statements
    IF,
    THEN,
    ELSE,
    SELECT,
    CASE,
    DEFAULT,

    ;

    public String getKeyword(boolean lowercase) {
        var keyword = name();
        return lowercase ? keyword.toLowerCase() : keyword;
    }
}
