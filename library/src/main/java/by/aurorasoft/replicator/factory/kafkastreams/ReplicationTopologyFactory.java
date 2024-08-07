package by.aurorasoft.replicator.factory.kafkastreams;

import by.aurorasoft.replicator.consuming.serde.ConsumingSerde;
import by.aurorasoft.replicator.model.setting.ReplicationConsumerSetting;
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

    public <E, ID> Topology create(ReplicationConsumerSetting<E, ID> setting) {
        StreamsBuilder builder = new StreamsBuilder();
        builder
                .stream(setting.getTopic(), with(getIdSerde(setting), getReplicationSerde(setting)))
                .foreach((id, replication) -> executeRetrying(replication, setting.getRepository()));
        return builder.build();
    }

    private <ID> ConsumingSerde<ID> getIdSerde(ReplicationConsumerSetting<?, ID> setting) {
        return new ConsumingSerde<>(setting.getIdDeserializer());
    }

    private <E, ID> ConsumingSerde<ConsumedReplication<E, ID>> getReplicationSerde(ReplicationConsumerSetting<E, ID> setting) {
        return new ConsumingSerde<>(new JsonDeserializer<>(setting.getReplicationTypeReference(), false));
    }

    private <E, ID> void executeRetrying(ConsumedReplication<E, ID> replication, JpaRepository<E, ID> repository) {
        retryTemplate.execute(context -> execute(replication, repository));
    }

    private <E, ID> Optional<?> execute(ConsumedReplication<E, ID> replication, JpaRepository<E, ID> repository) {
        replication.execute(repository);
        return empty();
    }
}
