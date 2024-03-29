package by.aurorasoft.replicator.consuming.starter;

import by.aurorasoft.replicator.consuming.pipeline.ReplicationConsumePipeline;
import by.aurorasoft.replicator.event.ReplicationTopicsCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class ReplicationConsumeStarter {
    private final List<ReplicationConsumePipeline<?, ?>> pipelines;
    private final ReplicationConsumePipelineStarter pipelineStarter;

    @EventListener(ReplicationTopicsCreatedEvent.class)
    public void start() {
        pipelines.forEach(pipelineStarter::start);
    }
}
