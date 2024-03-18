package by.aurorasoft.replicator.consuming.starter;

import by.aurorasoft.replicator.consuming.config.ReplicationConsumePipeline;
import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.stereotype.Component;

import static org.apache.kafka.streams.kstream.Consumed.with;

@Component
public final class ReplicationConsumingPipelineStarter {

    public <ID, E extends AbstractEntity<ID>> void start(final ReplicationConsumePipeline<ID, E> config,
                                                         final StreamsBuilder streamsBuilder) {
        streamsBuilder
                .stream(config.getTopic(), with(config.getIdSerde(), config.getReplicationSerde()))
                .foreach((id, replication) -> replication.execute(config.getRepository()));
    }
}
