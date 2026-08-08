package pt.up.fe.specs.fortran.parser;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Optional;

public abstract class ClassMapper<T extends FortranNode> {
    private final Class<T> superClass;

    public ClassMapper(Class<T> superClass) {
        this.superClass = superClass;
    }

    public Class<T> gerSuperClass() {
        return superClass;
    }

    public abstract Optional<Class<? extends T>> get(FlangAttributes attrs);
    public abstract boolean has(FlangAttributes attrs);

    public static <U extends FortranNode> AlwaysClassMapper<U> always(Class<U> superClass) {
        return new AlwaysClassMapper<>(superClass);
    }

    public static <U extends FortranNode> CaseClassMapper<U> caseFor(Class<U> superClass) {
        return new CaseClassMapper<>(superClass);
    }
}
