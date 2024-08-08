package by.aurorasoft.replicator.registry;

import by.aurorasoft.replicator.producer.ReplicationProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

//TODO: test
@RequiredArgsConstructor
public final class ReplicationProducerRegistry {
    private final Map<JpaRepository<?, ?>, ReplicationProducer> producersByRepositories;

    public Optional<ReplicationProducer> get(JpaRepository<?, ?> repository) {
        return ofNullable(producersByRepositories.get(repository));
    }
}
