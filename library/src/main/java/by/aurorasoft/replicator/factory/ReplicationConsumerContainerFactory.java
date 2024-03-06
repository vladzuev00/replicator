package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.consuming.consumer.ReplicationConsumer;
import by.aurorasoft.replicator.consuming.consumer.ReplicationConsumerConfig;
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
        final KafkaListenerContainerFactory<?> factory = createContainerFactory(consumer.getConfig());
        final KafkaListenerEndpoint endpoint = endpointFactory.create(consumer);
        return factory.createListenerContainer(endpoint);
    }

    private <ID, DTO extends AbstractDto<ID>> KafkaListenerContainerFactory<?> createContainerFactory(
            final ReplicationConsumerConfig<ID, DTO> config
    ) {
        final var factory = new ConcurrentKafkaListenerContainerFactory<ID, Replication<ID, DTO>>();
        factory.setConsumerFactory(createConsumerFactory(config));
        return factory;
    }

    private <ID, DTO extends AbstractDto<ID>> ConsumerFactory<ID, Replication<ID, DTO>> createConsumerFactory(
            final ReplicationConsumerConfig<ID, DTO> config
    ) {
        final Map<String, Object> configsByNames = createConfigsByNames(config);
        final var replicationDeserializer = replicationDeserializerFactory.createDeserializer(config);
        return new DefaultKafkaConsumerFactory<>(configsByNames, config.getIdDeserializer(), replicationDeserializer);
    }

    private Map<String, Object> createConfigsByNames(final ReplicationConsumerConfig<?, ?> config) {
        return Map.of(BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress, GROUP_ID_CONFIG, config.getGroupId());
    }
}
