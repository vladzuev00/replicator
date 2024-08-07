package by.aurorasoft.replicator.factory.registry;

import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.model.setting.ReplicationProducerSetting;
import by.aurorasoft.replicator.registry.KafkaTemplateRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public final class KafkaTemplateRegistryFactory extends RegistryFactory<JpaRepository<?, ?>, KafkaTemplate<Object, ProducedReplication<?>>, KafkaTemplateRegistry> {
    private final List<ReplicationProducerSetting<?, ?>> producerSettings;

    @Override
    protected Stream<KafkaTemplate<Object, ProducedReplication<?>>> createValues() {
        return null;
    }

    @Override
    protected JpaRepository<?, ?> getKey(KafkaTemplate<Object, ProducedReplication<?>> value) {
        return null;
    }

    @Override
    protected KafkaTemplateRegistry createInternal(Map<JpaRepository<?, ?>, KafkaTemplate<Object, ProducedReplication<?>>> valuesByKeys) {
        return null;
    }
}
