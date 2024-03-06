package by.aurorasoft.replicator.consuming;

import by.aurorasoft.replicator.consuming.consumer.KafkaReplicationConsumer;
import by.aurorasoft.replicator.consuming.consumer.KafkaReplicationConsumerConfig;
import by.aurorasoft.replicator.consuming.consumer.KafkaReplicationConsumerStarter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class ReplicationConsumingStarter {
    private final List<KafkaReplicationConsumerConfig<?, ?>> consumerConfigs;
    private final KafkaReplicationConsumerStarter consumerStarter;

    @EventListener(ContextRefreshedEvent.class)
    public void start() {
        consumerConfigs.stream()
                .map(KafkaReplicationConsumer::new)
                .forEach(consumerStarter::start);
    }
}
