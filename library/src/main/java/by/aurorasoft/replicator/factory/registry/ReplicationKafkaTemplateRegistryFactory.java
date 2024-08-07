package by.aurorasoft.replicator.factory.registry;

import by.aurorasoft.replicator.factory.kafkatemplate.ReplicationKafkaTemplateFactory;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.model.setting.ReplicationProducerSetting;
import by.aurorasoft.replicator.registry.ReplicationKafkaTemplateRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public final class ReplicationKafkaTemplateRegistryFactory extends RegistryFactory<JpaRepository<?, ?>, KafkaTemplate<Object, ProducedReplication<?>>, ReplicationKafkaTemplateRegistry> {
    private final List<ReplicationProducerSetting<?, ?>> producerSettings;
    private final ReplicationKafkaTemplateFactory kafkaTemplateFactory;

    @Override
    protected Stream<KafkaTemplate<Object, ProducedReplication<?>>> createValues() {
        return producerSettings.stream().map(kafkaTemplateFactory::create);
    }

    @Override
    protected JpaRepository<?, ?> getKey(KafkaTemplate<Object, ProducedReplication<?>> value) {
        return null;
    }

    @Override
    protected ReplicationKafkaTemplateRegistry createInternal(Map<JpaRepository<?, ?>, KafkaTemplate<Object, ProducedReplication<?>>> valuesByKeys) {
        return null;
    }
}
