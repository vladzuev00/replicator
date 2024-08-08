package by.aurorasoft.replicator.factory.registry;

import by.aurorasoft.replicator.factory.producer.ReplicationProducerFactory;
import by.aurorasoft.replicator.model.setting.ReplicationProducerSetting;
import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;

@Component
@RequiredArgsConstructor
public final class ReplicationProducerRegistryFactory {
    private final ReplicationProducerFactory producerFactory;
    private final List<ReplicationProducerSetting<?, ?>> producerSettings;

    public ReplicationProducerRegistry create() {
        return producerSettings.stream()
                .collect(
                        collectingAndThen(
                                toMap(ReplicationProducerSetting::getRepository, producerFactory::create),
                                ReplicationProducerRegistry::new
                        )
                );
    }
}
