package pt.up.fe.specs.fortran.ast;

public enum FortranKeyword {
    END,
    PROGRAM,
    CONCURRENT,
    SUBROUTINE,
    FUNCTION,
    USE,
    INTRINSIC,
    NON_INTRINSIC,
    ONLY,
    GOTO,
    EXTERNAL,
    STOP,

    // Execution statements
    PRINT,
    DO,
    WHILE,
    GO,
    TO,
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
    READ,
    WAIT,
    CLOSE,

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
    BIND,
    C,
    NAME,
    CDEFINED,
    RESULT,

    OMP;

    public String getKeyword(boolean lowercase) {
        var keyword = name();
        return lowercase ? keyword.toLowerCase() : keyword;
    }
}
