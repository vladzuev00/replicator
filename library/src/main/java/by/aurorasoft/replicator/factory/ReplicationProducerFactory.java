package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedRepository;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.Producer;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public final class ReplicationProducerFactory {
    private final ReplicationProducerConfigsFactory configsFactory;
    private final ReplicationProducerKeySerializerFactory keySerializerFactory;

    private final ReplicationKafkaTemplateFactory kafkaTemplateFactory;

    public ReplicationProducer create(String topicName, Producer config) {
        Map<String, Object> configsByKeys = configsFactory.create(config);
        Serializer<Object> keySerializer = keySerializerFactory.create(config);



        var kafkaTemplate = kafkaTemplateFactory.create(repository.producer());
        return new ReplicationProducer("topicName", kafkaTemplate);
    }
}
