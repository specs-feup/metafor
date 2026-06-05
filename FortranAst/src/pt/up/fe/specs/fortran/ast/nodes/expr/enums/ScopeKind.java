package pt.up.fe.specs.fortran.ast.nodes.expr.enums;

import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.enums.EnumHelper;
import pt.up.fe.specs.util.lazy.Lazy;

import java.util.Optional;

public enum ScopeKind {
    GLOBAL,
    INTRINSIC_MODULES,
    MODULE,
    MAIN_PROGRAM,
    SUBPROGRAM,
    BLOCK_DATA,
    DERIVED_TYPE,
    BLOCK_CONSTRUCT,
    FORALL,
    OTHER_CONSTRUCT,
    OPEN_ACC_CONSTRUCT("OpenACCConstruct"),
    IMPLIED_DOS,
    OTHER_CLAUSE;

    private static final Lazy<EnumHelper<ScopeKind>> HELPER = EnumHelper.newLazyHelper(ScopeKind.class);

    final String string;

    ScopeKind() {
        this.string = SpecsStrings.toCamelCase(name());
    }

    ScopeKind(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public static Optional<ScopeKind> of(String string) {
        return HELPER.get().fromNameTry(string);
    }
}
