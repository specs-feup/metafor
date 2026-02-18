package pt.up.fe.specs.fortran.ast.nodes.expr.enums;

import pt.up.fe.specs.util.providers.StringProvider;

public enum UnaryOperatorKind implements StringProvider {
    Plus,
    Minus,
    Not;

    public String getOpString() {
        switch (this) {
            case Plus -> {
                return "+";
            }
            case Minus -> {
                return "-";
            }
            case Not -> {
                return ".not.";
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
