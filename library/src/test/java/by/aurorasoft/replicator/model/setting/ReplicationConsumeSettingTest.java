package by.aurorasoft.replicator.model.setting;

import by.aurorasoft.replicator.model.replication.consumed.ConsumedReplication;
import by.aurorasoft.replicator.testcrud.TestEntity;
import by.aurorasoft.replicator.testcrud.TestRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public final class ReplicationConsumeSettingTest {

    @Test
    public void settingShouldBeCreated() {
        String givenTopic = "test-topic";
        JpaRepository<TestEntity, Long> givenRepository = mock(TestRepository.class);
        Deserializer<Long> givenIdDeserializer = new LongDeserializer();
        TypeReference<ConsumedReplication<TestEntity, Long>> givenReplicationTypeReference = new TypeReference<>() {
        };

        ReplicationConsumeSetting<TestEntity, Long> actual = new ReplicationConsumeSetting<>(
                givenTopic,
                givenRepository,
                givenIdDeserializer,
                givenReplicationTypeReference
        );

        String actualTopic = actual.getTopic();
        assertSame(givenTopic, actualTopic);

        JpaRepository<TestEntity, Long> actualRepository = actual.getRepository();
        assertSame(givenRepository, actualRepository);

        Deserializer<Long> actualIdDeserializer = actual.getIdDeserializer();
        assertSame(givenIdDeserializer, actualIdDeserializer);

        var actualReplicationTypeReference = actual.getReplicationTypeReference();
        assertSame(givenReplicationTypeReference, actualReplicationTypeReference);
    }

    @Test
    public void settingShouldNotBeCreatedBecauseOfTopicIsNull() {
        JpaRepository<TestEntity, Long> givenRepository = mock(TestRepository.class);
        @SuppressWarnings("resource") Deserializer<Long> givenIdDeserializer = new LongDeserializer();
        TypeReference<ConsumedReplication<TestEntity, Long>> givenReplicationTypeReference = new TypeReference<>() {
        };

        assertThrows(
                NullPointerException.class,
                () -> ReplicationConsumeSetting.<TestEntity, Long>builder()
                        .repository(givenRepository)
                        .idDeserializer(givenIdDeserializer)
                        .replicationTypeReference(givenReplicationTypeReference)
                        .build()
        );
    }

    @Test
    public void settingShouldNotBeCreatedBecauseOfRepositoryIsNull() {
        String givenTopic = "test-topic";
        @SuppressWarnings("resource") Deserializer<Long> givenIdDeserializer = new LongDeserializer();
        TypeReference<ConsumedReplication<TestEntity, Long>> givenReplicationTypeReference = new TypeReference<>() {
        };

        assertThrows(
                NullPointerException.class,
                () -> ReplicationConsumeSetting.<TestEntity, Long>builder()
                        .topic(givenTopic)
                        .idDeserializer(givenIdDeserializer)
                        .replicationTypeReference(givenReplicationTypeReference)
                        .build()
        );
    }

    @Test
    public void settingShouldNotBeCreatedBecauseOfIdDeserializerIsNull() {
        String givenTopic = "test-topic";
        JpaRepository<TestEntity, Long> givenRepository = mock(TestRepository.class);
        TypeReference<ConsumedReplication<TestEntity, Long>> givenReplicationTypeReference = new TypeReference<>() {
        };

        assertThrows(
                NullPointerException.class,
                () -> ReplicationConsumeSetting.<TestEntity, Long>builder()
                        .topic(givenTopic)
                        .repository(givenRepository)
                        .replicationTypeReference(givenReplicationTypeReference)
                        .build()
        );
    }

    @Test
    public void settingShouldNotBeCreatedBecauseOfReplicationTypeReferenceIsNull() {
        String givenTopic = "test-topic";
        JpaRepository<TestEntity, Long> givenRepository = mock(TestRepository.class);
        @SuppressWarnings("resource") Deserializer<Long> givenIdDeserializer = new LongDeserializer();

        assertThrows(
                NullPointerException.class,
                () -> ReplicationConsumeSetting.<TestEntity, Long>builder()
                        .topic(givenTopic)
                        .repository(givenRepository)
                        .idDeserializer(givenIdDeserializer)
                        .build()
        );
    }
}
