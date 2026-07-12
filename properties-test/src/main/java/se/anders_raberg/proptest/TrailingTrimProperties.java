package se.anders_raberg.proptest;

import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class TrailingTrimProperties extends Properties {

    @Override
    public synchronized Object put(Object key, Object value) {
        return super.put(key, trimTrailing(value));
    }

    @Override
    public synchronized Object putIfAbsent(Object key, Object value) {
        return super.putIfAbsent(key, trimTrailing(value));
    }

    @Override
    public synchronized void putAll(Map<?, ?> t) {
        super.putAll(t.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> trimTrailing(entry.getValue()))));
    }

    private Object trimTrailing(Object value) {
        return value instanceof String s ? s.replaceFirst("[\\t ]+$", "") : value;
    }

}
