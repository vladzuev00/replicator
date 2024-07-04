package by.aurorasoft.replicator.registry.producer;

import by.aurorasoft.replicator.producer.ReplicationProducer;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public final class ReplicationProducerRegistry {
    private final Map<Object, ReplicationProducer> producersByServices;

    public Optional<ReplicationProducer> get(final Object service) {
        return ofNullable(producersByServices.get(service));
    }
}
