package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.producer.ReplicationProducer;
import org.apache.kafka.streams.errors.StreamsException;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
import org.springframework.stereotype.Component;

import static org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder.newInstance;

//TODO: refactor and tests
@Component
public final class ReplicationTopicRetryConfigFactory {

    public RetryTopicConfiguration create(final ReplicationProducer<?> producer) {
        return newInstance()
                .includeTopic(producer.getTopicName())
                .retryOn(StreamsException.class)  //TODO: replace by suitable exception
                .maxAttempts(5)
                .fixedBackOff(1000)
                .create(producer.getKafkaTemplate());
    }
}
