package by.aurorasoft.replicator.model.pipeline;

import by.aurorasoft.replicator.model.replication.consumed.ConsumedReplication;
import by.aurorasoft.replicator.v2.entity.TestV2Entity;
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
        String givenTopic = "test-topic";
        Serde<Long> givenIdSerde = mock(Serde.class);
        var givenReplicationTypeReference = new TypeReference<ConsumedReplication<TestV2Entity, Long>>() {
        };
        JpaRepository<TestV2Entity, Long> givenRepository = mock(JpaRepository.class);

        var actual = new ReplicationConsumePipeline<>(
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
    @Test(expected = NullPointerException.class)
    public void pipelineShouldNotBeCreatedBecauseOfTopicIsNull() {
        ReplicationConsumePipeline.builder()
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
                .topic("test-topic")
                .idSerde(mock(Serde.class))
                .repository(mock(JpaRepository.class))
                .build();
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NullPointerException.class)
    public void pipelineShouldBeCreatedBecauseOfRepositoryIsNull() {
        ReplicationConsumePipeline.builder()
                .topic("test-topic")
                .idSerde(mock(Serde.class))
                .replicationTypeReference(new TypeReference<>() {
                })
                .build();
    }

    private TypeReference<?> getReplicationTypeReference(ReplicationConsumePipeline<?, ?> pipeline) {
        return getFieldValue(pipeline.getReplicationSerde(), REPLICATION_TYPE_REFERENCE_FIELD_NAME, TypeReference.class);
    }
}
