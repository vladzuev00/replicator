package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.registry.ReplicatedRepositoryRegistry;
import by.aurorasoft.replicator.registry.replicationproducer.ReplicationProducerRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toMap;

@Component
@RequiredArgsConstructor
public final class ReplicationProducerRegistryFactory {
    private final ReplicatedRepositoryRegistry repositoryRegistry;
//    private final ReplicationProducerFactory producerFactory;

    public ReplicationProducerRegistry create() {
        return null;
//        return repositoryRegistry.getRepositories()
//                .stream()
//                .collect(collectingAndThen(toMap(identity(), this::createProducer), ReplicationProducerRegistry::new));
    }

    private ReplicationProducer createProducer(JpaRepository<?, ?> repository) {
        return null;
//        ReplicatedRepository repositoryConfig = getAnnotation(repository.getClass(), ReplicatedRepository.class);
//        String topicName = repositoryConfig.topic().name();
//        Producer producerConfig = repositoryConfig.producer();
//        return producerFactory.create(topicName, producerConfig);
    }
}
