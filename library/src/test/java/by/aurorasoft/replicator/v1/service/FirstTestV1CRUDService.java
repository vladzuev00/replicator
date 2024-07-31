package by.aurorasoft.replicator.v1.service;

import by.aurorasoft.replicator.annotation.ReplicatedRepository;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.Producer;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.Topic;
import org.apache.kafka.common.serialization.LongSerializer;

@ReplicatedRepository(
        producer = @Producer(idSerializer = LongSerializer.class),
        topicConfig = @Topic(name = "first-topic")
)
public class FirstTestV1CRUDService extends TestV1CRUDService {

}
