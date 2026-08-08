package pt.up.fe.specs.fortran.ast.nodes.io.enums;

public enum LabelIoControlSpecKind {
    END,
    EOR,
    ERR;

    public static LabelIoControlSpecKind convert(String name) {
        return valueOf(name.toUpperCase());
    }
}
