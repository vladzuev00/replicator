package by.aurorasoft.replicator.v1.service;

import by.aurorasoft.replicator.annotation.ReplicatedRepository;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.Producer;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.Topic;
import org.apache.kafka.common.serialization.LongSerializer;

@ReplicatedRepository(
        producer = @Producer(
                idSerializer = LongSerializer.class,
                batchSize = 15,
                lingerMs = 515,
                deliveryTimeoutMs = 110000
        ),
        topicConfig = @Topic(
                name = "second-topic",
                partitionCount = 2,
                replicationFactor = 2
        )
)
public class SecondTestV1CRUDService extends TestV1CRUDService {

}
