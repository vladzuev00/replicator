package by.aurorasoft.replicator.holder.producer;

import by.aurorasoft.replicator.producer.ReplicationProducer;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public final class ReplicationProducerHolder {
    private final Map<Object, ReplicationProducer> producersByServices;

    public Optional<ReplicationProducer> findByService(final Object service) {
        return ofNullable(producersByServices.get(service));
    }
}
