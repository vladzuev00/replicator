package by.aurorasoft.replicator.consuming.starter.factory;

import by.aurorasoft.replicator.model.pipeline.ReplicationConsumePipeline;
import by.aurorasoft.replicator.model.replication.consumed.ConsumedReplication;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.apache.kafka.streams.kstream.Consumed.with;

@Component
public final class ReplicationTopologyFactory {
    private final RetryTemplate retryTemplate;

    public ReplicationTopologyFactory(@Qualifier("replicationRetryTemplate") final RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
    }

    public <E, ID> Topology create(final ReplicationConsumePipeline<E, ID> pipeline) {
        final StreamsBuilder builder = new StreamsBuilder();
        builder
                .stream(pipeline.getTopic(), with(pipeline.getIdSerde(), pipeline.getReplicationSerde()))
                .foreach((id, replication) -> executeRetrying(replication, pipeline.getRepository()));
        return builder.build();
    }

    private <E, ID> void executeRetrying(final ConsumedReplication<E, ID> replication,
                                         final JpaRepository<E, ID> repository) {
        retryTemplate.execute(context -> execute(replication, repository));
    }

    private <E, ID> Optional<?> execute(final ConsumedReplication<E, ID> replication,
                                        final JpaRepository<E, ID> repository) {
        replication.execute(repository);
        return empty();
    }
}
