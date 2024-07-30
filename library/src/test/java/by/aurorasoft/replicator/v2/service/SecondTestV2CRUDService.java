package by.aurorasoft.replicator.v2.service;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
import by.aurorasoft.replicator.annotation.ReplicatedService.ViewConfig;
import by.aurorasoft.replicator.v2.dto.TestV2Dto;
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
        ),
        viewConfigs = {
                @ViewConfig(
                        type = TestV2Dto.class,
                        includedFields = "first-field",
                        excludedFields = "second-field"
                ),
                @ViewConfig(
                        type = TestV2Dto.class,
                        includedFields = {"third-field", "fourth-field"},
                        excludedFields = {"fifth-field", "sixth-field", "seventh-field"}
                )
        }
)
public class SecondTestV2CRUDService extends TestV2CRUDService {

}
