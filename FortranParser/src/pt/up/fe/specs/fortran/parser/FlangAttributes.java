package pt.up.fe.specs.fortran.parser;

import pt.up.fe.specs.util.SpecsCollections;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.providers.StringProvider;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

public class FlangAttributes {

    private static final Set<String> EXPECTED_COMMON_KEYS = Set.of("id", "variantKey");

    private final Map<String, Object> attributes;

    public FlangAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return attributes.toString();
    }

    public Set<String> getKeys() {
        return attributes.keySet();
    }

    /*
        public Map<String, Object> getAttributes() {
            return attributes;
        }
    */
    public Optional<String> getOptionalString(String key) {
        var value = attributes.get(key);

        if (value == null) {
            return Optional.empty();
        }

        return Optional.of(value.toString());
    }

    public Optional<String> getOptionalString(Pattern pattern) {

        // Go over all keys in attributes, check which ones match the pattern

        var matches = attributes.keySet().stream()
                .filter(key -> pattern.matcher(key).matches())
                .toList();

        if (matches.size() > 1) {
            throw new RuntimeException("Given pattern '" + pattern + "' matches more than one attribute: " + attributes);
        }

        if (matches.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(attributes.get(matches.get(0)).toString());
    }


    public String getString(StringProvider key) {
        return getString(key.getString());
    }

    public String getString(String key) {
        return getOptionalString(key).orElseThrow(() -> new RuntimeException("No attribute '" + key + "': " + attributes));
    }

    public String getString(Pattern key) {
        return getOptionalString(key).orElseThrow(() -> new RuntimeException("No attribute that matches regex '" + key + "': " + attributes));
    }


    public List<String> getStringList(StringProvider key) {
        return getList(key, Object::toString);
    }

    public List<String> getStringList(String key) {
        return getList(key, Object::toString);
    }


    public <T> List<T> getList(StringProvider key, Function<Object, T> converter) {
        return getList(key.getString(), converter);
    }

    public <T> List<T> getList(String key, Function<Object, T> converter) {
        var value = attributes.get(key);
        Objects.requireNonNull(value, () -> "Attrs do not have a value for key '" + key + "': " + attributes);
        if (value instanceof List<?> list) {
            return list.stream().map(o -> converter.apply(o)).toList();
        }
        return List.of(converter.apply(value));
    }

    public Optional<Object> getOptional(String key) {
        var value = attributes.get(key);
        return Optional.ofNullable(value);
    }

    /**
     * Adds the given attributes to the current attributes, without overwritting existing attributes.
     *
     * @param givenAttributes
     */
    public void merge(FlangAttributes givenAttributes) {
        var currentKeys = this.attributes.keySet();
        var mergingKeys = givenAttributes.attributes.keySet();

        // Check intersection of keys
        var intersection = SpecsCollections.intersection(currentKeys, mergingKeys);
        // Remove expected common keys
        intersection.removeAll(EXPECTED_COMMON_KEYS);
        // Warn if not empty
        if (!intersection.isEmpty()) {
            SpecsLogs.info("There are unexpected common keys while merging attributes: " + intersection);
        }

        // Add non-existing keys from given attributes
        for (var key : mergingKeys) {

            // Do not merge these keys
            if (EXPECTED_COMMON_KEYS.contains(key)) {
                continue;
            }

            if (attributes.containsKey(key)) {
                continue;
            }

            attributes.put(key, givenAttributes.attributes.get(key));
        }
    }

    public boolean has(FlangName key) {
        return has(key.getString());
    }

    public boolean has(String key) {
        return getKeys().contains(key);
    }
}
