package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.consuming.consumer.ReplicationConsumer;
import by.aurorasoft.replicator.model.Replication;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.apache.kafka.clients.admin.AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;

@Component
public final class ReplicationConsumerContainerFactory {
    private final ReplicationConsumerEndpointFactory endpointFactory;
    private final ReplicationDeserializerFactory replicationDeserializerFactory;
    private final String bootstrapAddress;

    public ReplicationConsumerContainerFactory(final ReplicationConsumerEndpointFactory endpointFactory,
                                               final ReplicationDeserializerFactory replicationDeserializerFactory,
                                               @Value("${spring.kafka.bootstrap-servers}") final String bootstrapAddress) {
        this.endpointFactory = endpointFactory;
        this.replicationDeserializerFactory = replicationDeserializerFactory;
        this.bootstrapAddress = bootstrapAddress;
    }

    public MessageListenerContainer create(final ReplicationConsumer<?, ?> consumer) {
        final KafkaListenerContainerFactory<?> factory = createContainerFactory(consumer);
        final KafkaListenerEndpoint endpoint = endpointFactory.create(consumer);
        return factory.createListenerContainer(endpoint);
    }

    private <ID, DTO extends AbstractDto<ID>> KafkaListenerContainerFactory<?> createContainerFactory(
            final ReplicationConsumer<ID, DTO> consumer
    ) {
        final var containerFactory = new ConcurrentKafkaListenerContainerFactory<ID, Replication<ID, DTO>>();
        final ConsumerFactory<ID, Replication<ID, DTO>> consumerFactory = createConsumerFactory(consumer);
        containerFactory.setConsumerFactory(consumerFactory);
        return containerFactory;
    }

    private <ID, DTO extends AbstractDto<ID>> ConsumerFactory<ID, Replication<ID, DTO>> createConsumerFactory(
            final ReplicationConsumer<ID, DTO> consumer
    ) {
        final Map<String, Object> configsByNames = getConfigsByNames(consumer);
        final var replicationDeserializer = replicationDeserializerFactory.create(consumer);
        return new DefaultKafkaConsumerFactory<>(configsByNames, consumer.getIdDeserializer(), replicationDeserializer);
    }

    private <ID, DTO extends AbstractDto<ID>> Map<String, Object> getConfigsByNames(
            final ReplicationConsumer<ID, DTO> consumer
    ) {
        return Map.of(BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress, GROUP_ID_CONFIG, consumer.getGroupId());
    }
}
