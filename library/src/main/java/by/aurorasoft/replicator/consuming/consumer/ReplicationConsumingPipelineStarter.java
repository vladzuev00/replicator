package by.aurorasoft.replicator.consuming.consumer;

import by.aurorasoft.replicator.consuming.ReplicationConsumingPipeline;
import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import static org.apache.kafka.streams.kstream.Consumed.with;

@Component
public final class ReplicationConsumingPipelineStarter<ID, E extends AbstractEntity<ID>> {

    public void start(final ReplicationConsumingPipeline<ID, E> pipeline, final StreamsBuilder streamsBuilder) {
        streamsBuilder
                .stream(pipeline.getTopic(), with(pipeline.getIdSerde(), pipeline.getReplicationSerde()))
                .foreach((id, replication) -> replication.execute(pipeline.getRepository()));
    }
}
