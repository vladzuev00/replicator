package by.aurorasoft.replicator.consuming.consumer;

import by.aurorasoft.replicator.model.consumed.ConsumedReplication;
import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.kafka.support.serializer.JsonSerde;

import static org.apache.kafka.streams.kstream.Consumed.with;

public final class ReplicationConsumingPipelineStarter<ID, E extends AbstractEntity<ID>> {
    private String topic;
    private Serde<ID> idSerde;
    private TypeReference<ConsumedReplication<ID, E>> replicationTypeReference;
    private JpaRepository<E, ID> repository;

    public void build(final StreamsBuilder streamsBuilder) {
        streamsBuilder
                .stream(topic, with(idSerde, new JsonSerde<>(replicationTypeReference)))
                .foreach((id, replication) -> replication.execute(repository));
    }
}
