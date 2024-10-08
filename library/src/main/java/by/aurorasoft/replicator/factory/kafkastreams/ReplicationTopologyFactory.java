package by.aurorasoft.replicator.factory.kafkastreams;

import by.aurorasoft.replicator.model.replication.consumed.ConsumedReplication;
import by.aurorasoft.replicator.model.setting.ReplicationConsumeSetting;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
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

    public <E, ID> Topology create(ReplicationConsumeSetting<E, ID> setting) {
        StreamsBuilder builder = new StreamsBuilder();
        builder
                .stream(setting.getTopic(), with(getIdSerde(setting), getReplicationSerde(setting)))
                .foreach((id, replication) -> executeRetrying(replication, setting.getRepository()));
        return builder.build();
    }

    private <ID> ConsumingSerde<ID> getIdSerde(ReplicationConsumeSetting<?, ID> setting) {
        return new ConsumingSerde<>(setting.getIdDeserializer());
    }

    private <E, ID> ConsumingSerde<ConsumedReplication<E, ID>> getReplicationSerde(ReplicationConsumeSetting<E, ID> setting) {
        return new ConsumingSerde<>(new JsonDeserializer<>(setting.getReplicationTypeReference(), false));
    }

    private <E, ID> void executeRetrying(ConsumedReplication<E, ID> replication, JpaRepository<E, ID> repository) {
        retryTemplate.execute(context -> execute(replication, repository));
    }

    private <E, ID> Optional<Void> execute(ConsumedReplication<E, ID> replication, JpaRepository<E, ID> repository) {
        replication.execute(repository);
        return empty();
    }

    @RequiredArgsConstructor
    private static final class ConsumingSerde<T> implements Serde<T> {
        private final Deserializer<T> deserializer;

        @Override
        public Serializer<T> serializer() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Deserializer<T> deserializer() {
            return deserializer;
        }
    }
}
