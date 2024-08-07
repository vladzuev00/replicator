package by.aurorasoft.replicator.factory.registry;

import by.aurorasoft.replicator.registry.Registry;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;

public abstract class RegistryFactory<K, V, R extends Registry<K, V>> {

    public final R create() {
        return createValues().collect(collectingAndThen(toMap(this::getKey, identity()), this::createInternal));
    }

    protected abstract Stream<V> createValues();

    protected abstract K getKey(V value);

    protected abstract R createInternal(Map<K, V> valuesByKeys);
}
