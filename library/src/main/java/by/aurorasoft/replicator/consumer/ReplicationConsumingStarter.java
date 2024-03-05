package by.aurorasoft.replicator.consumer;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class ReplicationConsumingStarter {
    private final List<KafkaReplicationConsumerConfig<?, ?>> consumerConfigs;
    private final KafkaReplicationConsumerStarter consumerStarter;

    public ReplicationConsumingStarter(final List<KafkaReplicationConsumerConfig<?, ?>> consumerConfigs,
                                       final KafkaReplicationConsumerStarter consumerStarter) {
        this.consumerConfigs = consumerConfigs;
        this.consumerStarter = consumerStarter;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void start() {
        consumerConfigs.stream()
                .map(this::createConsumer)
                .forEach(consumerStarter::start);
    }

    private <ID, DTO extends AbstractDto<ID>> KafkaReplicationConsumer<ID, DTO> createConsumer(
            final KafkaReplicationConsumerConfig<ID, DTO> config
    ) {
        return new KafkaReplicationConsumer<>(
                config.getService(),
                config.getGroupId(),
                config.getTopic(),
                config.getIdDeserializer()
        );
    }
}
