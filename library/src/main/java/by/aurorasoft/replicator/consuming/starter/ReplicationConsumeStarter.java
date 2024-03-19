package by.aurorasoft.replicator.consuming.starter;

import by.aurorasoft.replicator.consuming.pipeline.ReplicationConsumePipeline;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class ReplicationConsumeStarter {
    private final List<ReplicationConsumePipeline<?, ?>> pipelines;
    private final ReplicationConsumePipelineStarter pipelineStarter;
    private final StreamsBuilder streamsBuilder;

    @PostConstruct
    public void start() {
        pipelines.forEach(this::start);
    }

    private void start(final ReplicationConsumePipeline<?, ?> pipeline) {
        pipelineStarter.start(pipeline, streamsBuilder);
    }
}
