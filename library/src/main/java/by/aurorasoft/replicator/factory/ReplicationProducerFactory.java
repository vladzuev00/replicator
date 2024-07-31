package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedRepository;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ReplicationProducerFactory {
    private final ReplicationKafkaTemplateFactory kafkaTemplateFactory;

    public ReplicationProducer create(ReplicatedRepository service) {
        String topicName = service.topicConfig().name();
        var kafkaTemplate = kafkaTemplateFactory.create(service.producer());
        return new ReplicationProducer(topicName, kafkaTemplate);
    }
}
