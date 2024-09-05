package by.aurorasoft.replicator.consumingstarter;

import by.aurorasoft.replicator.factory.kafkastreams.ReplicationKafkaStreamsFactory;
import by.aurorasoft.replicator.model.setting.ReplicationConsumeSetting;
import by.aurorasoft.replicator.validator.ReplicationUniqueComponentCheckingManager;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KafkaStreams;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class ReplicationConsumingStarter {
    private final ReplicationUniqueComponentCheckingManager uniqueComponentCheckingManager;
    private final ReplicationKafkaStreamsFactory kafkaStreamsFactory;
    private final List<ReplicationConsumeSetting<?, ?>> settings;

    @EventListener(ContextRefreshedEvent.class)
    public void start() {
//        uniqueComponentCheckingManager.check(settings);
        settings.stream()
                .map(kafkaStreamsFactory::create)
                .forEach(KafkaStreams::start);
    }
}
