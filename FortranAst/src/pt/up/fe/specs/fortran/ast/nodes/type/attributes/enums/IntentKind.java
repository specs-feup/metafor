package pt.up.fe.specs.fortran.ast.nodes.type.attributes.enums;

import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.enums.EnumHelper;
import pt.up.fe.specs.util.lazy.Lazy;
import pt.up.fe.specs.util.providers.StringProvider;

import java.util.Optional;

public enum IntentKind implements StringProvider {
    IN,
    OUT,
    IN_OUT;

    private final String string;

    private static final Lazy<EnumHelper<IntentKind>> HELPER = EnumHelper.newLazyHelper(IntentKind.class);

    IntentKind() {
        this.string = SpecsStrings.toCamelCase(name());
    }

    public static Optional<IntentKind> convertTry(String name) {
        return HELPER.get().fromNameTry(name);
    }

    @Override
    public String getString() {
        return this.string;
    }
}
