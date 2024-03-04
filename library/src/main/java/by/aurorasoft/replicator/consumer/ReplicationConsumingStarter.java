package by.aurorasoft.replicator.consumer;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class ReplicationConsumingStarter {
    private final String bootstrapAddress;
    private final ObjectMapper objectMapper;
    private final List<KafkaReplicationConsumerConfig<?, ?>> consumerConfigs;

    public ReplicationConsumingStarter(@Value("${spring.kafka.bootstrap-servers}") final String bootstrapAddress,
                                       final ObjectMapper objectMapper,
                                       final List<KafkaReplicationConsumerConfig<?, ?>> consumerConfigs) {
        this.bootstrapAddress = bootstrapAddress;
        this.objectMapper = objectMapper;
        this.consumerConfigs = consumerConfigs;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void start() {
        consumerConfigs.stream()
                .map(this::createConsumer)
                .forEach(KafkaReplicationConsumer::start);
    }

    private <ID, DTO extends AbstractDto<ID>> KafkaReplicationConsumer<ID, DTO> createConsumer(
            final KafkaReplicationConsumerConfig<ID, DTO> config
    ) {
        return new KafkaReplicationConsumer<>(
                config.getService(),
                objectMapper,
                config.getDtoType(),
                bootstrapAddress,
                config.getGroupId(),
                config.getTopic(),
                config.getIdDeserializer()
        );
    }
}
