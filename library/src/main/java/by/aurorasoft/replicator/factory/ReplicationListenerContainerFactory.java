package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.consuming.consumer.KafkaReplicationConsumerConfig;
import by.aurorasoft.replicator.model.Replication;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.apache.kafka.clients.admin.AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;

@Component
public final class ReplicationListenerContainerFactory {
    private final ReplicationDeserializerFactory deserializerFactory;
    private final String bootstrapAddress;

    public ReplicationListenerContainerFactory(final ReplicationDeserializerFactory deserializerFactory,
                                               @Value("${spring.kafka.bootstrap-servers}") final String bootstrapAddress) {
        this.deserializerFactory = deserializerFactory;
        this.bootstrapAddress = bootstrapAddress;
    }

    public <ID, DTO extends AbstractDto<ID>> ConcurrentKafkaListenerContainerFactory<ID, Replication<ID, DTO>> create(
            final KafkaReplicationConsumerConfig<ID, DTO> config
    ) {
        final var factory = new ConcurrentKafkaListenerContainerFactory<ID, Replication<ID, DTO>>();
        factory.setConsumerFactory(createConsumerFactory(config));
        return factory;
    }

    private <ID, DTO extends AbstractDto<ID>> ConsumerFactory<ID, Replication<ID, DTO>> createConsumerFactory(
            final KafkaReplicationConsumerConfig<ID, DTO> config
    ) {
        return new DefaultKafkaConsumerFactory<>(
                createConfigsByNames(config),
                config.getIdDeserializer(),
                deserializerFactory.createDeserializer(config)
        );
    }

    private Map<String, Object> createConfigsByNames(final KafkaReplicationConsumerConfig<?, ?> config) {
        return Map.of(BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress, GROUP_ID_CONFIG, config.getGroupId());
    }

}
