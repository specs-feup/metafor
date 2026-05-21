package pt.up.fe.specs.fortran.ast;

public enum FortranKeyword {
    END,
    PROGRAM,
    CONCURRENT,
    SUBROUTINE,
    WRITE,
    USE,

    // Execution statements
    PRINT,
    DO,
    WHILE,
    GO,
    TO,
    STOP,
    ERROR,
    QUIET,

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

    // Declarations
    PARAMETER,

    OMP;

    public String getKeyword(boolean lowercase) {
        var keyword = name();
        return lowercase ? keyword.toLowerCase() : keyword;
    }
}
