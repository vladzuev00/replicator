package by.aurorasoft.replicator.registry;

import by.aurorasoft.replicator.producer.KafkaReplicationProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public final class ReplicationProducerRegistry {
    private final Map<JpaRepository<?, ?>, KafkaReplicationProducer> producersByRepositories;

    public Optional<KafkaReplicationProducer> get(JpaRepository<?, ?> repository) {
        return ofNullable(producersByRepositories.get(repository));
    }
}
