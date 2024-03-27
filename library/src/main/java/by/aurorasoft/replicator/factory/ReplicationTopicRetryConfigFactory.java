package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.exception.PerhapsRelationNotDeliveredYetException;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
import org.springframework.stereotype.Component;

import static org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder.newInstance;

@Component
public final class ReplicationTopicRetryConfigFactory {

    public RetryTopicConfiguration create(final ReplicationProducer<?> producer) {
        return newInstance()
                .includeTopic(producer.getTopicName())
                .retryOn(PerhapsRelationNotDeliveredYetException.class)
                .create(producer.getKafkaTemplate());
    }
}
