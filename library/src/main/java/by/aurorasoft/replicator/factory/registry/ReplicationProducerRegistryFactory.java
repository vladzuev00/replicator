package by.aurorasoft.replicator.factory.registry;

import by.aurorasoft.replicator.factory.producer.ReplicationProducerFactory;
import by.aurorasoft.replicator.model.setting.ReplicationProduceSetting;
import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
import by.aurorasoft.replicator.validator.ReplicationUniqueComponentCheckingManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;

@Component
@RequiredArgsConstructor
public final class ReplicationProducerRegistryFactory {
    private final ReplicationUniqueComponentCheckingManager uniqueComponentCheckingManager;
    private final ReplicationProducerFactory producerFactory;

    public ReplicationProducerRegistry create(List<ReplicationProduceSetting<?, ?>> settings) {
        uniqueComponentCheckingManager.check(settings);
        return settings.stream()
                .collect(
                        collectingAndThen(
                                toMap(ReplicationProduceSetting::getRepository, producerFactory::create),
                                ReplicationProducerRegistry::new
                        )
                );
    }
}
