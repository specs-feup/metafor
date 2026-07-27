package pt.up.fe.specs.fortran.ast.nodes.io.enums;

public enum VarConnectSpecKind {
    IOMSG,
    IOSTAT,
    NEWUNIT;

    public static VarConnectSpecKind convert(String name) {
        return valueOf(name.toUpperCase());
    }
}
