package by.aurorasoft.replicator.v1.service;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
import org.apache.kafka.common.serialization.LongSerializer;

@ReplicatedService(
        producerConfig = @ProducerConfig(
                idSerializer = LongSerializer.class,
                batchSize = 15,
                lingerMs = 515,
                deliveryTimeoutMs = 110000
        ),
        topicConfig = @TopicConfig(
                name = "second-topic",
                partitionCount = 2,
                replicationFactor = 2
        )
)
public class SecondTestV1CRUDService extends TestV1CRUDService {

}
