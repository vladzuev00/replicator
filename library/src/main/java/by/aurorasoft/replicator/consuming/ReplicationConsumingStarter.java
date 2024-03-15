package by.aurorasoft.replicator.consuming;

import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

//TODO: refactor
@Component
public final class ReplicationConsumingStarter {
    private final List<ReplicationConsumingPipeline<?, ?>> pipelines;
    private final ReplicationConsumingPipelineStarter pipelineStarter;
    private final StreamsBuilder streamsBuilder;

    public ReplicationConsumingStarter(List<ReplicationConsumingPipeline<?, ?>> pipelines, ReplicationConsumingPipelineStarter pipelineStarter, StreamsBuilder streamsBuilder) {
        this.pipelines = pipelines;
        this.pipelineStarter = pipelineStarter;
        this.streamsBuilder = streamsBuilder;
        pipelines.forEach(this::start);
    }


//    @EventListener(ContextRefreshedEvent.class)
    public void start() {
        pipelines.forEach(this::start);
    }

    private void start(final ReplicationConsumingPipeline<?, ?> pipeline) {
        pipelineStarter.start(pipeline, streamsBuilder);
    }
}
