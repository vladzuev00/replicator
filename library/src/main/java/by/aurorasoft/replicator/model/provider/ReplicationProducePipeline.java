package by.aurorasoft.replicator.model.provider;

import by.aurorasoft.replicator.model.config.EntityViewConfig;
import by.aurorasoft.replicator.model.config.ProducerConfig;
import by.aurorasoft.replicator.model.config.TopicConfig;
import lombok.Value;

@Value
public class ReplicationProducePipeline {
    ProducerConfig producerConfig;
    TopicConfig topicConfig;
    EntityViewConfig[] entityViewConfigs;
}
