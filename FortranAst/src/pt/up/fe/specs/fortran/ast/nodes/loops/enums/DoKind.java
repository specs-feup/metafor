package pt.up.fe.specs.fortran.ast.nodes.loops.enums;

import pt.up.fe.specs.util.providers.StringProvider;

public enum DoKind implements StringProvider {
    RANGE,
    WHILE;

    @Override
    public String getString() {
        return this.toString();
    }
}
