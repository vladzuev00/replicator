package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.consuming.serde.ConsumingSerde;
import by.aurorasoft.replicator.model.setting.ReplicationConsumerConfig;
import by.aurorasoft.replicator.model.replication.consumed.ConsumedReplication;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.apache.kafka.streams.kstream.Consumed.with;

@Component
public final class ReplicationTopologyFactory {
    private final RetryTemplate retryTemplate;

    public ReplicationTopologyFactory(@Qualifier("replicationRetryTemplate") RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
    }

    public <E, ID> Topology create(ReplicationConsumerConfig<E, ID> pipeline) {
        StreamsBuilder builder = new StreamsBuilder();
        builder
                .stream(pipeline.getTopic(), with(getIdSerde(pipeline), getReplicationSerde(pipeline)))
                .foreach((id, replication) -> executeRetrying(replication, pipeline.getRepository()));
        return builder.build();
    }

    private <ID> ConsumingSerde<ID> getIdSerde(ReplicationConsumerConfig<?, ID> pipeline) {
        return new ConsumingSerde<>(pipeline.getIdDeserializer());
    }

    private <E, ID> ConsumingSerde<ConsumedReplication<E, ID>> getReplicationSerde(ReplicationConsumerConfig<E, ID> pipeline) {
        return new ConsumingSerde<>(new JsonDeserializer<>(pipeline.getReplicationTypeReference(), false));
    }

    private <E, ID> void executeRetrying(ConsumedReplication<E, ID> replication, JpaRepository<E, ID> repository) {
        retryTemplate.execute(context -> execute(replication, repository));
    }

    private <E, ID> Optional<?> execute(ConsumedReplication<E, ID> replication, JpaRepository<E, ID> repository) {
        replication.execute(repository);
        return empty();
    }
}
