package pt.up.fe.specs.fortran.ast;

public enum FortranKeyword {
    END,
    PROGRAM,
    PRINT,
    DO,
    WHILE,
    CONCURRENT,
    SUBROUTINE,
    WRITE,
    USE,
    GOTO,
    COMMON,
    EXTERNAL,

    // Conditional statements
    IF,
    THEN,
    ELSE,
    SELECT,
    CASE,
    DEFAULT,

    OMP;

    public String getKeyword(boolean lowercase) {
        var keyword = name();
        return lowercase ? keyword.toLowerCase() : keyword;
    }
}
