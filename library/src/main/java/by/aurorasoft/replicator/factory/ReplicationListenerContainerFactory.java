package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.consumer.KafkaReplicationConsumerConfig;
import by.aurorasoft.replicator.deserializer.ReplicationDeserializer;
import by.aurorasoft.replicator.model.Replication;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;

import static org.apache.kafka.clients.admin.AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;

public final class ReplicationListenerContainerFactory {
    private final ObjectMapper objectMapper;

    private <ID, DTO extends AbstractDto<ID>> ConcurrentKafkaListenerContainerFactory<ID, Replication<ID, DTO>> createListenerContainerFactory(
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
                createDeserializer(config)
        );
    }

    private <ID, DTO extends AbstractDto<ID>> Map<String, Object> createConfigsByNames(final KafkaReplicationConsumerConfig<ID, DTO> config) {
        return Map.of(BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress, GROUP_ID_CONFIG, config.getGroupId());
    }

}
