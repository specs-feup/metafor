package pt.up.fe.specs.fortran.ast.nodes.utils.enums;

import pt.up.fe.specs.util.providers.StringProvider;

public enum IoControlSpecKind implements StringProvider {
    ADVANCE;

    public String getKindCode() {
        switch (this) {
            case ADVANCE -> {
                return "advance";
            }
            default -> {
                return "<UNDEFINED_IO_CONTROL_SPEC_STRING:" + this + ">";
            }
        }
    }

    @Override
    public String getString() {
        return getKindCode();
    }
}
