package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedRepository;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.registry.ReplicatedRepositoryRegistry;
import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;

@Component
@RequiredArgsConstructor
public final class ReplicationProducerRegistryFactory {
    private final ReplicatedRepositoryRegistry serviceRegistry;
    private final ReplicationProducerFactory producerFactory;

    public ReplicationProducerRegistry create() {
        return serviceRegistry.getServices()
                .stream()
                .collect(collectingAndThen(toMap(identity(), this::createProducer), ReplicationProducerRegistry::new));
    }

    private ReplicationProducer createProducer(Object service) {
        ReplicatedRepository annotation = service.getClass().getAnnotation(ReplicatedRepository.class);
        return producerFactory.create(annotation);
    }
}
