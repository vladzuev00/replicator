package by.aurorasoft.replicator.holder;

import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public final class ReplicationProducerHolder {
    private final Map<AbsServiceRUD<?, ?, ?, ?, ?>, ReplicationProducer<?, ?>> producersByServices;

    public Optional<ReplicationProducer<?, ?>> findByService(final AbsServiceRUD<?, ?, ?, ?, ?> service) {
        return ofNullable(producersByServices.get(service));
    }
}
