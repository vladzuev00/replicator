package by.aurorasoft.replicator.factory.registry;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import by.aurorasoft.replicator.factory.producer.ReplicationProducerFactory;
import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
import by.aurorasoft.replicator.validator.ReplicatedServiceUniqueTopicValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;

@Component
@RequiredArgsConstructor
public final class ReplicationProducerRegistryFactory {
//    private final ReplicatedServiceUniqueTopicValidator uniqueTopicValidator;
    private final ApplicationContext applicationContext;
    private final ReplicationProducerFactory producerFactory;

    public ReplicationProducerRegistry create() {
//        var replicatedServices = applicationContext.getBeansWithAnnotation(ReplicatedService.class).values();
//        uniqueTopicValidator.validate(replicatedServices);
        //TODO: validate unique topics
        return applicationContext.getBeansWithAnnotation(ReplicatedService.class)
                .values()
                .stream()
                .collect(
                        collectingAndThen(
                                toMap(identity(), producerFactory::create),
                                ReplicationProducerRegistry::new
                        )
                );
    }
}
