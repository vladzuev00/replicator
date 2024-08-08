package by.aurorasoft.replicator.factory.producer;

import by.aurorasoft.replicator.factory.kafkatemplate.ReplicationKafkaTemplateFactory;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.model.setting.ReplicationProducerSetting;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ReplicationProducerFactory {
    private final ReplicationKafkaTemplateFactory kafkaTemplateFactory;

    public ReplicationProducer create(ReplicationProducerSetting<?, ?> setting) {
        KafkaTemplate<Object, ProducedReplication<?>> kafkaTemplate = kafkaTemplateFactory.create(setting);
        return new ReplicationProducer(kafkaTemplate, setting.getTopic(), setting.getEntityViewSettings());
    }
}
