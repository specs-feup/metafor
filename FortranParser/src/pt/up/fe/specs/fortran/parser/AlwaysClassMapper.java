package pt.up.fe.specs.fortran.parser;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Optional;

public class AlwaysClassMapper<T extends FortranNode> extends ClassMapper<T> {
    private final Class<T> clazz;

    public AlwaysClassMapper(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Optional<Class<? extends T>> get(FlangAttributes attrs) {
        return Optional.of(clazz);
    }

    @Override
    public boolean has(FlangAttributes attrs) {
        return true;
    }
}
