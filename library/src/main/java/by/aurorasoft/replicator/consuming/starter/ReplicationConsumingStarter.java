package by.aurorasoft.replicator.consuming.starter;

import by.aurorasoft.replicator.consuming.config.ReplicationConsumePipelineConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class ReplicationConsumingStarter {
    private final List<ReplicationConsumePipelineConfig<?, ?>> pipelineConfigs;
    private final ReplicationConsumingPipelineStarter pipelineStarter;
    private final StreamsBuilder streamsBuilder;

    @PostConstruct
    public void start() {
        pipelineConfigs.forEach(this::startPipeline);
    }

    private void startPipeline(final ReplicationConsumePipelineConfig<?, ?> config) {
        pipelineStarter.start(config, streamsBuilder);
    }
}
