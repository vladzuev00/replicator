package by.aurorasoft.replicator.consuming.starter;

import by.aurorasoft.replicator.consuming.pipeline.ReplicationConsumePipeline;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class ReplicationConsumeStarter {
    private final List<ReplicationConsumePipeline<?, ?>> pipelines;
    private final ReplicationConsumePipelineStarter pipelineStarter;

    @PostConstruct
    public void start() {
        pipelines.forEach(pipelineStarter::start);
    }
}
