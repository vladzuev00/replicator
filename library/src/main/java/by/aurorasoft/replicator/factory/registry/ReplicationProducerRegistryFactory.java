package by.aurorasoft.replicator.factory.registry;

import by.aurorasoft.replicator.loader.ReplicatedServiceLoader;
import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
import by.aurorasoft.replicator.starter.ReplicationProducingStarter;
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
    private final ReplicationProducingStarter producingStarter;

    public ReplicationProducerRegistry create() {
        Collection<Object> services = serviceLoader.load();
        uniqueTopicValidator.validate(services);
        return services.stream()
                .collect(
                        collectingAndThen(
                                toMap(identity(), producingStarter::startReturningProducer),
                                ReplicationProducerRegistry::new
                        )
                );
    }
}
