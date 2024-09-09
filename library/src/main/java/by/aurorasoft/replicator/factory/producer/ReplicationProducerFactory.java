package by.aurorasoft.replicator.factory.producer;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import by.aurorasoft.replicator.factory.kafkatemplate.ReplicationKafkaTemplateFactory;
import by.aurorasoft.replicator.factory.replication.SaveProducedReplicationFactory;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ReplicationProducerFactory {
    private final ReplicationKafkaTemplateFactory kafkaTemplateFactory;
    private final SaveProducedReplicationFactory saveReplicationFactory;

    public ReplicationProducer create(ReplicatedService service) {
        var kafkaTemplate = kafkaTemplateFactory.create(service.producerConfig());
        return new ReplicationProducer(
                saveReplicationFactory,
                kafkaTemplate,
                service.topicConfig().name(),
                service.dtoViewConfigs()
        );
    }
}
