package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.registry.producer.ReplicationProducerRegistry;
import by.aurorasoft.replicator.registry.service.ReplicatedServiceRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;

@Component
@RequiredArgsConstructor
public final class ReplicationProducerRegistryFactory {
    private final ReplicatedServiceRegistry serviceRegistry;
    private final ReplicationProducerFactory producerFactory;

    public ReplicationProducerRegistry create() {
        return serviceRegistry.getServices()
                .stream()
                .collect(collectingAndThen(toMap(identity(), producerFactory::create), ReplicationProducerRegistry::new));
    }
}
