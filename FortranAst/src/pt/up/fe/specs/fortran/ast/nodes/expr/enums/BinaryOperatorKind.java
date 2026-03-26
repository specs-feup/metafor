package pt.up.fe.specs.fortran.ast.nodes.expr.enums;

import pt.up.fe.specs.util.providers.StringProvider;

public enum BinaryOperatorKind implements StringProvider {
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE,
    LT,
    LE,
    GT,
    GE,
    EQ,
    NE;

    public String getOpString() {
        switch (this) {
            case ADD -> {
                return "+";
            }
            case SUBTRACT -> {
                return "-";
            }
            case MULTIPLY -> {
                return "*";
            }
            case DIVIDE -> {
                return "/";
            }
            case EQ -> {
                return "==";
            }
            case NE -> {
                return "/=";
            }
            case GE -> {
                return ">=";
            }
            case GT -> {
                return ">";
            }
            case LE -> {
                return "<=";
            }
            case LT -> {
                return "<";
            }
            default -> {
                return "<UNDEFINED_BINARY_OP_STRING:" + this + ">";
            }
        }
    }

    @Override
    public String getString() {
        return getOpString();
    }
}
