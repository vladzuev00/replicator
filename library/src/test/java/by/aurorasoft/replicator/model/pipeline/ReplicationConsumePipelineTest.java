package by.aurorasoft.replicator.model.pipeline;

import by.aurorasoft.replicator.base.v2.entity.TestEntity;
import by.aurorasoft.replicator.model.replication.consumed.ConsumedReplication;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.common.serialization.Serde;
import org.junit.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import static by.aurorasoft.replicator.testutil.ReflectionUtil.getFieldValue;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public final class ReplicationConsumePipelineTest {
    private static final String REPLICATION_TYPE_REFERENCE_FIELD_NAME = "replicationTypeReference";

    @Test
    @SuppressWarnings("unchecked")
    public void pipelineShouldBeCreated() {
        final String givenId = "test-pipeline";
        final String givenTopic = "test-topic";
        final Serde<Long> givenIdSerde = mock(Serde.class);
        final TypeReference<ConsumedReplication<Long, TestEntity>> givenReplicationTypeReference = new TypeReference<>() {
        };
        final JpaRepository<TestEntity, Long> givenRepository = mock(JpaRepository.class);

        final var actual = new ReplicationConsumePipeline<>(
                givenId,
                givenTopic,
                givenIdSerde,
                givenReplicationTypeReference,
                givenRepository
        );

        assertSame(givenId, actual.getId());
        assertSame(givenTopic, actual.getTopic());
        assertSame(givenIdSerde, actual.getIdSerde());
        assertSame(givenRepository, actual.getRepository());
        assertSame(givenReplicationTypeReference, getReplicationTypeReference(actual));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NullPointerException.class)
    public void pipelineShouldNotBeCreatedBecauseOfIdIsNull() {
        ReplicationConsumePipeline.builder()
                .topic("test-topic")
                .idSerde(mock(Serde.class))
                .replicationTypeReference(new TypeReference<>() {
                })
                .repository(mock(JpaRepository.class))
                .build();
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NullPointerException.class)
    public void pipelineShouldBeCreatedBecauseOfTopicIsNull() {
        ReplicationConsumePipeline.builder()
                .id("test-pipeline")
                .idSerde(mock(Serde.class))
                .replicationTypeReference(new TypeReference<>() {
                })
                .repository(mock(JpaRepository.class))
                .build();
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NullPointerException.class)
    public void pipelineShouldBeCreatedBecauseOfIdSerdeIsNull() {
        ReplicationConsumePipeline.builder()
                .id("test-pipeline")
                .topic("test-topic")
                .replicationTypeReference(new TypeReference<>() {
                })
                .repository(mock(JpaRepository.class))
                .build();
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NullPointerException.class)
    public void pipelineShouldBeCreatedBecauseOfReplicationTypeReferenceIsNull() {
        ReplicationConsumePipeline.builder()
                .id("test-pipeline")
                .topic("test-topic")
                .idSerde(mock(Serde.class))
                .repository(mock(JpaRepository.class))
                .build();
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NullPointerException.class)
    public void pipelineShouldBeCreatedBecauseOfRepositoryIsNull() {
        ReplicationConsumePipeline.builder()
                .id("test-pipeline")
                .topic("test-topic")
                .idSerde(mock(Serde.class))
                .replicationTypeReference(new TypeReference<>() {
                })
                .build();
    }

    @SuppressWarnings("unchecked")
    private static TypeReference<ConsumedReplication<Long, TestEntity>> getReplicationTypeReference(
            final ReplicationConsumePipeline<Long, TestEntity> pipeline
    ) {
        return getFieldValue(pipeline.getReplicationSerde(), REPLICATION_TYPE_REFERENCE_FIELD_NAME, TypeReference.class);
    }
}
