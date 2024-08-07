package by.aurorasoft.replicator.factory.kafkatemplate;

import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.model.setting.ReplicationProducerSetting;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public final class ReplicationKafkaTemplateFactory {

    public KafkaTemplate<Object, ProducedReplication<?>> create(ReplicationProducerSetting<?, ?> setting) {
        Map<String, Object> configsByKeys = configsFactory.create(config);
//        Serializer<Object> keySerializer = keySerializerFactory.create(config);
//        Serializer<ProducedReplication<?>> valueSerializer = valueSerializerFactory.create();
//        var producerFactory = new DefaultKafkaProducerFactory<>(configsByKeys, keySerializer, valueSerializer);
//        KafkaTemplate<Object, ProducedReplication<?>> kafkaTemplate = new KafkaTemplate<>(producerFactory);
//        return new ReplicationProducer(topicName, kafkaTemplate);
    }
}
