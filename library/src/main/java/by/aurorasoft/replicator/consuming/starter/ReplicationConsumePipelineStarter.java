package by.aurorasoft.replicator.consuming.starter;

import by.aurorasoft.replicator.factory.ReplicationKafkaStreamsFactory;
import by.aurorasoft.replicator.model.setting.ReplicationConsumerConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ReplicationConsumePipelineStarter {
    private final ReplicationKafkaStreamsFactory streamsFactory;

    public void start(ReplicationConsumerConfig<?, ?> pipeline) {
        streamsFactory.create(pipeline).start();
    }
}
