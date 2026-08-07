package pt.up.fe.specs.fortran.ast.nodes.decl.enums;

public enum ComponentAttrKind {
    ALLOCATABLE,
    CONTIGUOUS,
    POINTER,

    // CUDA
    CONSTANT,
    DEVICE,
    MANAGED,
    PINNED,
    SHARED,
    TEXTURE,
    UNIFIED;
}
