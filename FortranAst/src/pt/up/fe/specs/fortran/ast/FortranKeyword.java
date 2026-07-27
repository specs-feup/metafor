package pt.up.fe.specs.fortran.ast;

public enum FortranKeyword {
    END,
    PROGRAM,
    CONCURRENT,
    SUBROUTINE,
    USE,
    INTRINSIC,
    NON_INTRINSIC,
    ONLY,

    // Execution statements
    PRINT,
    DO,
    WHILE,
    GO,
    TO,
    STOP,
    ERROR,
    QUIET,
    CALL,
    CONTINUE,
    CONTAINS,
    ALLOCATE,
    DEALLOCATE,
    RETURN,
    OPEN,
    ERR,
    WRITE,
    UNIT,
    FMT,
    NML,
    REWIND,
    NAMELIST,

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
    COMPLEX,

    // Declarations
    PARAMETER,
    DATA,
    COMMON,
    INTENT,
    STAT,
    LEN,
    KIND,

    OMP;

    public String getKeyword(boolean lowercase) {
        var keyword = name();
        return lowercase ? keyword.toLowerCase() : keyword;
    }
}
