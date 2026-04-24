package pt.up.fe.specs.fortran.ast.nodes.expr.enums;

import pt.up.fe.specs.util.providers.StringProvider;

public enum UnaryOperatorKind implements StringProvider {
    NEGATE("-"),
    UNARY_PLUS("+"),
    NOT(".not. ");

    UnaryOperatorKind(String opString) {
        this.opString = opString;
    }

    private final String opString;

    public String getString() {
        return opString;
    }
}
