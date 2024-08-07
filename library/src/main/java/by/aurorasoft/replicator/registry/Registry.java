package by.aurorasoft.replicator.registry;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public abstract class Registry<K, V> {
    private final Map<K, V> valuesByKeys;

    public final Optional<V> get(K key) {
        return ofNullable(valuesByKeys.get(key));
    }
}
