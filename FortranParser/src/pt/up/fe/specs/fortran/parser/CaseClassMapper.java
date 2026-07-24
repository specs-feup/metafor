package pt.up.fe.specs.fortran.parser;

import pt.up.fe.specs.fortran.ast.nodes.FortranNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class CaseClassMapper<T extends FortranNode> extends ClassMapper<T> {
    private final Map<String, Optional<Class<? extends T>>> caseMap;

    public CaseClassMapper(Map<String, Optional<Class<? extends T>>> caseMap) {
        this.caseMap = new HashMap<>(caseMap);
    }

    public CaseClassMapper() {
        this.caseMap = new HashMap<>();
    }

    public CaseClassMapper<T> map(FlangName variantKey, Class<? extends T> clazz) {
        caseMap.put(variantKey.getString(), Optional.of(clazz));
        return this;
    }

    public CaseClassMapper<T> ignore(FlangName variantKey) {
        caseMap.put(variantKey.getString(), Optional.empty());
        return this;
    }

    @Override
    public Optional<Class<? extends T>> get(FlangAttributes attrs) {
        var variantKey = attrs.getVariantKey();
        var clazz = caseMap.get(variantKey);
        Objects.requireNonNull(clazz, () -> "Could not find variant node for node with variant key '"
            + variantKey + "'");

        return clazz;
    }

    @Override
    public boolean has(FlangAttributes attrs) {
        var clazz = caseMap.get(attrs.getVariantKey());
        return clazz != null && clazz.isPresent();
    }
}
