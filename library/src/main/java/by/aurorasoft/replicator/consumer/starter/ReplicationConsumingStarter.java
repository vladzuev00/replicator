package by.aurorasoft.replicator.consumer.starter;

import by.aurorasoft.replicator.consumer.KafkaReplicationConsumer;
import by.aurorasoft.replicator.consumer.KafkaReplicationConsumerConfig;
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
                .map(KafkaReplicationConsumer::new)
                .forEach(consumerStarter::start);
    }
}
