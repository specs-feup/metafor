package pt.up.fe.specs.fortran.ast.nodes.expr.enums;

import pt.up.fe.specs.util.providers.StringProvider;

public enum UnaryOperatorKind implements StringProvider {
    NEGATE("-"),
    UNARY_PLUS("+"),
    NOT(".NOT. ");

    private final String opString;

    UnaryOperatorKind(String opString) {
        this.opString = opString;
    }

    public String getString() {
        return opString;
    }
}
