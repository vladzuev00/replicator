package by.aurorasoft.replicator.factory.producer;

import by.aurorasoft.replicator.factory.kafkatemplate.ReplicationKafkaTemplateFactory;
import by.aurorasoft.replicator.factory.replication.SaveProducedReplicationFactory;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.model.setting.ReplicationProduceSetting;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.transaction.manager.ReplicationTransactionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ReplicationProducerFactory {
    private final ReplicationKafkaTemplateFactory kafkaTemplateFactory;
    private final SaveProducedReplicationFactory saveReplicationFactory;
    private final ReplicationTransactionManager transactionManager;

    public ReplicationProducer create(ReplicationProduceSetting<?, ?> setting) {
        KafkaTemplate<Object, ProducedReplication<?>> kafkaTemplate = kafkaTemplateFactory.create(setting);
        return new ReplicationProducer(
                saveReplicationFactory,
                kafkaTemplate,
                transactionManager,
                setting.getTopic(),
                setting.getEntityViewSettings()
        );
    }
}
