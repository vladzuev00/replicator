package by.aurorasoft.replicator.consuming;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class ReplicationConsumingStarter {
    private final List<ReplicationConsumingPipelineConfig<?, ?>> pipelineConfigs;
    private final ReplicationConsumingPipelineStarter pipelineStarter;
    private final StreamsBuilder streamsBuilder;

    @PostConstruct
    public void start() {
        pipelineConfigs.forEach(this::startPipeline);
    }

    private void startPipeline(final ReplicationConsumingPipelineConfig<?, ?> config) {
        pipelineStarter.start(config, streamsBuilder);
    }
}
