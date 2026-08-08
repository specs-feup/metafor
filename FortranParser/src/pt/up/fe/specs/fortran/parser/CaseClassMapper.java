package pt.up.fe.specs.fortran.parser;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class CaseClassMapper<T extends FortranNode> extends ClassMapper<T> {
    private final Map<String, Optional<Class<? extends T>>> caseMap;

    public CaseClassMapper(Class<T> clazz, Map<String, Optional<Class<? extends T>>> caseMap) {
        super(clazz);
        this.caseMap = new HashMap<>(caseMap);
    }

    public CaseClassMapper(Class<T> clazz) {
        this(clazz, Map.of());
    }

    public CaseClassMapper<T> map(String variantKey, Class<? extends T> clazz) {
        caseMap.put(variantKey, Optional.of(clazz));
        return this;
    }

    public CaseClassMapper<T> map(FlangName variantKey, Class<? extends T> clazz) {
        return map(variantKey.getString(), clazz);
    }

    public CaseClassMapper<T> ignore(String variantKey) {
        caseMap.put(variantKey, Optional.empty());
        return this;
    }

    public CaseClassMapper<T> ignore(FlangName variantKey) {
        return ignore(variantKey.getString());
    }

    @Override
    public Optional<Class<? extends T>> get(FlangAttributes attrs) {
        var variantKey = attrs.getVariantKey();
        var clazz = caseMap.get(variantKey);
        Objects.requireNonNull(clazz, () -> "Could not find variant node of '" + gerSuperClass().getName()
                + "' for variant key '" + variantKey + "'");

        return clazz;
    }

    @Override
    public boolean has(FlangAttributes attrs) {
        var clazz = caseMap.get(attrs.getVariantKey());
        return clazz != null && clazz.isPresent();
    }
}
