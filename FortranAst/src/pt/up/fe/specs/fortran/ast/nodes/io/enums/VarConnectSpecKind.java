package pt.up.fe.specs.fortran.ast.nodes.io.enums;

import pt.up.fe.specs.fortran.ast.nodes.io.VarConnectSpec;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.providers.StringProvider;

public enum VarConnectSpecKind implements StringProvider {
    IOMSG,
    IOSTAT,
    NEWUNIT;

    public final String string;

    VarConnectSpecKind() {
        string = SpecsStrings.toCamelCase(name());
    }

    @Override
    public String getString() {
        return string;
    }
}
