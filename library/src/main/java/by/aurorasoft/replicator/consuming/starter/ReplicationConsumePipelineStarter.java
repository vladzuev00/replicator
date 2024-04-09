package by.aurorasoft.replicator.consuming.starter;

import by.aurorasoft.replicator.consuming.pipeline.ReplicationConsumePipeline;
import by.aurorasoft.replicator.factory.ReplicationTopologyFactory;
import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ReplicationConsumePipelineStarter {
    private final ReplicationTopologyFactory streamsFactory;

    public <ID, E extends AbstractEntity<ID>> void start(final ReplicationConsumePipeline<ID, E> pipeline) {
        streamsFactory.create(pipeline).start();
    }
}
