package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ReplicationProducerFactory {
    private final ReplicationKafkaTemplateFactory kafkaTemplateFactory;

    public ReplicationProducer create(ReplicatedService service) {
        String topicName = service.topicConfig().name();
        var kafkaTemplate = kafkaTemplateFactory.create(service.producerConfig());
        return new ReplicationProducer(topicName, kafkaTemplate);
    }
}
