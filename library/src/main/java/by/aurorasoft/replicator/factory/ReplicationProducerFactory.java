package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedRepository.Producer;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.producer.KafkaReplicationProducer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public final class ReplicationProducerFactory {
    private final ReplicationProducerConfigsFactory configsFactory;
    private final ReplicationProducerKeySerializerFactory keySerializerFactory;
    private final ReplicationProducerValueSerializerFactory valueSerializerFactory;

    public KafkaReplicationProducer create(String topicName, Producer config) {
        Map<String, Object> configsByKeys = configsFactory.create(config);
        Serializer<Object> keySerializer = keySerializerFactory.create(config);
        Serializer<ProducedReplication<?>> valueSerializer = valueSerializerFactory.create();
        var producerFactory = new DefaultKafkaProducerFactory<>(configsByKeys, keySerializer, valueSerializer);
        KafkaTemplate<Object, ProducedReplication<?>> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        return new KafkaReplicationProducer(topicName, kafkaTemplate);
    }
}
