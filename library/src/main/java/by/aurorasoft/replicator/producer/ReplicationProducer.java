package by.aurorasoft.replicator.producer;

import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.model.setting.ReplicationProducerSetting;
import by.aurorasoft.replicator.model.setting.ReplicationProducerSetting.EntityViewSetting;
import by.aurorasoft.replicator.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;

import static by.aurorasoft.replicator.util.IdUtil.getId;

@RequiredArgsConstructor
public final class ReplicationProducer {
    private final KafkaTemplate<Object, ProducedReplication<?>> kafkaTemplate;
    private final String topicName;
    private final EntityViewSetting[] entityViewSettings;

    public void produceSave(Object savedEntity) {
//        Object entityId = getId(savedEntity);
//        ProducerRecord<Object, ProducedReplication<?>> record = new ProducerRecord<>(topicName, entityId, replication);
//        kafkaTemplate.send(record);
    }

    public void produceDelete(Object entityId) {

    }
}
