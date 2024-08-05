package by.aurorasoft.replicator.model.config;

import lombok.Value;
import org.apache.kafka.common.serialization.Serializer;

@Value
public class ProducerConfig {
    Class<? extends Serializer<?>> idSerializer;
    int batchSize;
    int lingerMs;
    int deliveryTimeoutMs;
}
