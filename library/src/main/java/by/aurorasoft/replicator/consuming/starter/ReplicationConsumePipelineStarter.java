package by.aurorasoft.replicator.consuming.starter;

import by.aurorasoft.replicator.consuming.starter.factory.ReplicationKafkaStreamsFactory;
import by.aurorasoft.replicator.model.pipeline.ReplicationConsumePipeline;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ReplicationConsumePipelineStarter {
    private final ReplicationKafkaStreamsFactory streamsFactory;

    public void start(final ReplicationConsumePipeline<?, ?> pipeline) {
        streamsFactory.create(pipeline).start();
    }
}
