package by.aurorasoft.replicator.consuming;

import by.aurorasoft.replicator.consuming.consumer.ReplicationConsumer;
import by.aurorasoft.replicator.consuming.consumer.ReplicationConsumerConfig;
import by.aurorasoft.replicator.consuming.consumer.ReplicationConsumerStarter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class ReplicationConsumingStarter {
    private final List<ReplicationConsumerConfig<?, ?>> consumerConfigs;
    private final ReplicationConsumerStarter consumerStarter;

    @EventListener(ContextRefreshedEvent.class)
    public void start() {
        consumerConfigs.stream()
                .map(ReplicationConsumer::new)
                .forEach(consumerStarter::start);
    }
}
