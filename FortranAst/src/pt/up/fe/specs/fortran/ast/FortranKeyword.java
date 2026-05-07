package pt.up.fe.specs.fortran.ast;

public enum FortranKeyword {
    END,
    PROGRAM,
    CONCURRENT,
    SUBROUTINE,

    // Execution statements
    PRINT,
    DO,
    WHILE,
    GO,
    TO,

    // Conditional statements
    IF,
    THEN,
    ELSE,
    SELECT,
    CASE,
    DEFAULT,

    // Types
    INTEGER,
    REAL,
    CHARACTER,
    LOGICAL,
    DOUBLE,
    PRECISION,
    DIMENSION,
    ;

    public String getKeyword(boolean lowercase) {
        var keyword = name();
        return lowercase ? keyword.toLowerCase() : keyword;
    }
}
