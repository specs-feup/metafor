package pt.up.fe.specs.fortran.ast.nodes.loops.enums;

import pt.up.fe.specs.fortran.ast.nodes.loops.ConcurrentLoopControl;
import pt.up.fe.specs.fortran.ast.nodes.loops.LoopControl;
import pt.up.fe.specs.fortran.ast.nodes.loops.RangeLoopControl;
import pt.up.fe.specs.fortran.ast.nodes.loops.WhileLoopControl;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.enums.EnumHelper;
import pt.up.fe.specs.util.lazy.Lazy;
import pt.up.fe.specs.util.providers.StringProvider;

import java.util.Optional;


public enum DoKind implements StringProvider {
    RANGE,
    WHILE,
    CONCURRENT;

    public static DoKind getKindFromControl(LoopControl control) {
        if (control instanceof RangeLoopControl) {
            return RANGE;
        }
        else if (control instanceof WhileLoopControl) {
            return WHILE;
        }
        else if (control instanceof ConcurrentLoopControl) {
            return CONCURRENT;
        }

        return null;
    }

    private static final Lazy<EnumHelper<DoKind>> HELPER = EnumHelper.newLazyHelper(DoKind.class);

    private final String string;

    DoKind() {
        this.string = SpecsStrings.toCamelCase(name());
    }

    @Override
    public String getString() {
        return string;
    }

    public static Optional<DoKind> convertTry(String name) {
        return HELPER.get().fromNameTry(name);
    }
}
