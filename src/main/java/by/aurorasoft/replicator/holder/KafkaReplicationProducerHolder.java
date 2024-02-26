package by.aurorasoft.replicator.holder;

import by.aurorasoft.replicator.producer.KafkaProducerReplication;
import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public final class KafkaReplicationProducerHolder {
    private final Map<AbsServiceRUD<?, ?, ?, ?, ?>, KafkaProducerReplication<?, ?>> producersByServices;

    public Optional<KafkaProducerReplication<?, ?>> findByService(final AbsServiceRUD<?, ?, ?, ?, ?> service) {
        return ofNullable(producersByServices.get(service));
    }
}
