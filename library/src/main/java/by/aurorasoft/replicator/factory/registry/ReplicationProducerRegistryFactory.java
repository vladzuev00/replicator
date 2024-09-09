package by.aurorasoft.replicator.factory.registry;

import by.aurorasoft.replicator.factory.producer.ReplicationProducerFactory;
import by.aurorasoft.replicator.loader.ReplicatedServiceLoader;
import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
import by.aurorasoft.replicator.topicallocator.ReplicationTopicAllocator;
import by.aurorasoft.replicator.validator.ReplicatedServiceUniqueTopicValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;

@Component
@RequiredArgsConstructor
public final class ReplicationProducerRegistryFactory {
    private final ReplicatedServiceUniqueTopicValidator uniqueTopicValidator;
    private final ReplicatedServiceLoader serviceLoader;
    private final ReplicationTopicAllocator topicAllocator;
    private final ReplicationProducerFactory producerFactory;

    public ReplicationProducerRegistry create() {
        throw new UnsupportedOperationException();
//        Collection<Object> services = serviceLoader.load();
//        uniqueTopicValidator.validate(services);
//        return services.stream()
//                .collect(
//                        collectingAndThen(
//                                toMap(identity(), producerFactory::create),
//                                ReplicationProducerRegistry::new
//                        )
//                );
    }
}
