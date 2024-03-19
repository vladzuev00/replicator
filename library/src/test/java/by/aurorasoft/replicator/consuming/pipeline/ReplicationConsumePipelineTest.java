package by.aurorasoft.replicator.consuming.pipeline;

import by.aurorasoft.replicator.base.entity.TestEntity;
import by.aurorasoft.replicator.model.consumed.ConsumedReplication;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.common.serialization.Serde;
import org.junit.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import static by.aurorasoft.replicator.util.ReflectionUtil.getFieldValue;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public final class ReplicationConsumePipelineTest {
    private static final String REPLICATION_TYPE_REFERENCE_FIELD_NAME = "replicationTypeReference";

    @Test
    @SuppressWarnings("unchecked")
    public void pipelineShouldBeCreated() {
        final String givenTopic = "topic";
        final Serde<Long> givenIdSerde = mock(Serde.class);
        final TypeReference<ConsumedReplication<Long, TestEntity>> givenReplicationTypeReference = new TypeReference<>() {
        };
        final JpaRepository<TestEntity, Long> givenRepository = mock(JpaRepository.class);

        final var actual = new ReplicationConsumePipeline<>(
                givenTopic,
                givenIdSerde,
                givenReplicationTypeReference,
                givenRepository
        );

        assertSame(givenTopic, actual.getTopic());
        assertSame(givenIdSerde, actual.getIdSerde());
        assertSame(givenRepository, actual.getRepository());
        assertSame(givenReplicationTypeReference, getReplicationTypeReference(actual));
    }

    @SuppressWarnings("unchecked")
    private static TypeReference<ConsumedReplication<Long, TestEntity>> getReplicationTypeReference(
            final ReplicationConsumePipeline<Long, TestEntity> pipeline
    ) {
        return getFieldValue(pipeline.getReplicationSerde(), REPLICATION_TYPE_REFERENCE_FIELD_NAME, TypeReference.class);
    }
}
