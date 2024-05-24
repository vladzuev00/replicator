package by.aurorasoft.replicator.v2.service;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
import org.apache.kafka.common.serialization.LongSerializer;

@ReplicatedService(
        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
        topicConfig = @TopicConfig(name = "first-topic")
)
public class FirstTestV2CRUDService extends TestV2CRUDService {

}
