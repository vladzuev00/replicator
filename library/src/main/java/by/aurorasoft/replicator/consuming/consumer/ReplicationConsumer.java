package by.aurorasoft.replicator.consuming.consumer;

import by.aurorasoft.kafka.consumer.KafkaConsumerAbstract;
import by.aurorasoft.replicator.model.consumed.ConsumedReplication;
import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.data.jpa.repository.JpaRepository;

@RequiredArgsConstructor
@Getter
public final class ReplicationConsumer<ID, E extends AbstractEntity<ID>> extends KafkaConsumerAbstract<ID, ConsumedReplication<ID, E>> {
    private final String groupId;
    private final String topic;
    private final Deserializer<ID> idDeserializer;
    private final TypeReference<ConsumedReplication<ID, E>> replicationTypeReference;
    private final JpaRepository<E, ID> repository;

    @Override
    public void listen(final ConsumerRecord<ID, ConsumedReplication<ID, E>> record) {
        record.value().execute(repository);
    }
}
