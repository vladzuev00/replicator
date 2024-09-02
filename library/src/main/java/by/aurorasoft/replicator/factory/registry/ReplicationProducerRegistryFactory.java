package by.aurorasoft.replicator.factory.registry;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.factory.producer.ReplicationProducerFactory;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;

@Component
@RequiredArgsConstructor
public final class ReplicationProducerRegistryFactory {
    private final ApplicationContext applicationContext;
    private final ReplicationProducerFactory producerFactory;

    public ReplicationProducerRegistry create() {
        return applicationContext.getBeansWithAnnotation(ReplicatedService.class)
                .values()
                .stream()
                .collect(collectingAndThen(toMap(identity(), this::createProducer), ReplicationProducerRegistry::new));
    }

    private ReplicationProducer createProducer(Object service) {
        ReplicatedService annotation = service.getClass().getAnnotation(ReplicatedService.class);
        return producerFactory.create(annotation);
    }
}
