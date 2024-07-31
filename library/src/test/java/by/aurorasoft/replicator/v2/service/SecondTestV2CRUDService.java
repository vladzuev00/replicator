package by.aurorasoft.replicator.v2.service;

import by.aurorasoft.replicator.annotation.ReplicatedRepository;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.Producer;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.Topic;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.EntityView;
import by.aurorasoft.replicator.v2.dto.TestV2Dto;
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
        ),
        entityViews = {
                @EntityView(
                        type = TestV2Dto.class,
                        includedFields = "first-field",
                        excludedFields = "second-field"
                ),
                @EntityView(
                        type = TestV2Dto.class,
                        includedFields = {"third-field", "fourth-field"},
                        excludedFields = {"fifth-field", "sixth-field", "seventh-field"}
                )
        }
)
public class SecondTestV2CRUDService extends TestV2CRUDService {

}
