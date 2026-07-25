package pt.up.fe.specs.fortran.ast.nodes.io.enums;

import pt.up.fe.specs.fortran.ast.nodes.io.VarIoControlSpec;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.providers.StringProvider;

public enum VarIoControlSpecKind implements StringProvider {
    ID,
    IOMSG,
    IOSTAT,
    SIZE,
    UNIT;

    private final String string;

    VarIoControlSpecKind() {
        string = SpecsStrings.toCamelCase(name());
    }

    @Override
    public String getString() {
        return string;
    }
}
