package by.aurorasoft.replicator.consuming.starter;

import by.aurorasoft.replicator.factory.ReplicationKafkaStreamsFactory;
import by.aurorasoft.replicator.model.setting.ReplicationConsumerSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ReplicationConsumePipelineStarter {
    private final ReplicationKafkaStreamsFactory streamsFactory;

    public void start(ReplicationConsumerSetting<?, ?> pipeline) {
        streamsFactory.create(pipeline).start();
    }
}
