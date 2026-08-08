package pt.up.fe.specs.fortran.ast.nodes.io.enums;

public enum VarIoControlSpecKind {
    ID,
    IOMSG,
    IOSTAT,
    SIZE,
    UNIT;

    public static VarIoControlSpecKind convert(String name) {
        return valueOf(name.toUpperCase());
    }
}
