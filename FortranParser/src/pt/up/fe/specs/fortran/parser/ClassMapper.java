package pt.up.fe.specs.fortran.parser;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Optional;

public abstract class ClassMapper<T extends FortranNode> {
    public abstract Optional<Class<? extends T>> get(FlangAttributes attrs);
    public abstract boolean has(FlangAttributes attrs);

    public static <U extends FortranNode> AlwaysClassMapper<U> always(Class<U> clazz) {
        return new AlwaysClassMapper<>(clazz);
    }

    public static <U extends FortranNode> CaseClassMapper<U> caseFor(Class<U> ignoredClazz) {
        return new CaseClassMapper<>();
    }
}
