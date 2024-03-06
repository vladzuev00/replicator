package by.aurorasoft.replicator.consumer.starter;

import by.aurorasoft.replicator.consumer.KafkaReplicationConsumer;
import by.aurorasoft.replicator.consumer.KafkaReplicationConsumerConfig;
import by.aurorasoft.replicator.deserializer.ReplicationDeserializer;
import by.aurorasoft.replicator.factory.ReplicationListenerEndpointFactory;
import by.aurorasoft.replicator.model.Replication;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.apache.kafka.clients.admin.AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;

//TODO: remove generics
@Component
public final class KafkaReplicationConsumerStarter {
    private final String bootstrapAddress;
    private final ObjectMapper objectMapper;
    private final ReplicationListenerEndpointFactory endpointFactory;

    public KafkaReplicationConsumerStarter(@Value("${spring.kafka.bootstrap-servers}") final String bootstrapAddress,
                                           final ObjectMapper objectMapper,
                                           final ReplicationListenerEndpointFactory endpointFactory) {
        this.bootstrapAddress = bootstrapAddress;
        this.objectMapper = objectMapper;
        this.endpointFactory = endpointFactory;
    }

    public <ID, DTO extends AbstractDto<ID>> void start(final KafkaReplicationConsumer<ID, DTO> consumer) {
        final KafkaReplicationConsumerConfig<ID, DTO> config = consumer.getConfig();
        createListenerContainerFactory(config)
                .createListenerContainer(endpointFactory.create(consumer))
                .start();
    }

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
                createReplicationDeserializer(config)
        );
    }

    private <ID, DTO extends AbstractDto<ID>> Map<String, Object> createConfigsByNames(final KafkaReplicationConsumerConfig<ID, DTO> config) {
        return Map.of(BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress, GROUP_ID_CONFIG, config.getGroupId());
    }

    private <ID, DTO extends AbstractDto<ID>> ReplicationDeserializer<ID, DTO> createReplicationDeserializer(final KafkaReplicationConsumerConfig<ID, DTO> config) {
        return new ReplicationDeserializer<>(config.getReplicationTypeReference(), objectMapper);
    }
}
