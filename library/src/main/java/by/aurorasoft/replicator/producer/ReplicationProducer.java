package by.aurorasoft.replicator.producer;

import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.model.setting.ReplicationProducerSetting.EntityViewSetting;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
@Getter
public final class ReplicationProducer {
    private final KafkaTemplate<Object, ProducedReplication<?>> kafkaTemplate;
    private final String topic;
    private final EntityViewSetting[] entityViewSettings;

    public void produceSave(Object savedEntity) {
//        Object entityId = getId(savedEntity);
//        ProducerRecord<Object, ProducedReplication<?>> record = new ProducerRecord<>(topicName, entityId, replication);
//        kafkaTemplate.send(record);
    }

    public void produceDelete(Object entityId) {

    }
}
