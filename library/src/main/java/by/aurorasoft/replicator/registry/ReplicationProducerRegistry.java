package by.aurorasoft.replicator.registry;

import by.aurorasoft.replicator.producer.ReplicationProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public abstract class ReplicationProducerRegistry<P extends ReplicationProducer<?>> {
    private final Map<JpaRepository<?, ?>, P> producersByRepositories;

    public Optional<P> get(JpaRepository<?, ?> repository) {
        return ofNullable(producersByRepositories.get(repository));
    }
}
