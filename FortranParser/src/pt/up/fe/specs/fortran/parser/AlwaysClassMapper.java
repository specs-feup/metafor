package pt.up.fe.specs.fortran.parser;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.Optional;

public class AlwaysClassMapper<T extends FortranNode> extends ClassMapper<T> {
    public AlwaysClassMapper(Class<T> clazz) {
        super(clazz);
    }

    @Override
    public Optional<Class<? extends T>> get(FlangAttributes attrs) {
        return Optional.of(gerSuperClass());
    }

    @Override
    public boolean has(FlangAttributes attrs) {
        return true;
    }
}
