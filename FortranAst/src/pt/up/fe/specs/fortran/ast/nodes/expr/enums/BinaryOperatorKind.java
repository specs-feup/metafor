package pt.up.fe.specs.fortran.ast.nodes.expr.enums;

import pt.up.fe.specs.util.providers.StringProvider;

public enum BinaryOperatorKind implements StringProvider {
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    LT("<"),
    LE("<="),
    GT(">"),
    GE(">="),
    EQ("=="),
    NE("/="),
    AND(".AND.");

    private final String opString;

    BinaryOperatorKind(String opString) {
        this.opString = opString;
    }

    @Override
    public String getString() {
        return opString;
    }
}
