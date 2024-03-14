package by.aurorasoft.replicator.consuming.deserializer;

import by.aurorasoft.replicator.base.entity.TestEntity;
import by.aurorasoft.replicator.model.consumed.ConsumedReplication;
import by.aurorasoft.replicator.model.consumed.DeleteConsumedReplication;
import by.aurorasoft.replicator.model.consumed.SaveConsumedReplication;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static java.util.UUID.fromString;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ReplicationDeserializerTest {
    private final ReplicationDeserializer<Long, TestEntity> deserializer = createDeserializer();

    @ParameterizedTest
    @MethodSource("provideJsonAndExpectedReplication")
    public void replicationShouldBeDeserialized(final String givenJson,
                                                final ConsumedReplication<Long, TestEntity> expected) {
        final String givenTopicName = "topic";
        final byte[] givenJsonBytes = givenJson.getBytes();

        final ConsumedReplication<Long, TestEntity> actual = deserializer.deserialize(givenTopicName, givenJsonBytes);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> provideJsonAndExpectedReplication() {
        return Stream.of(
                Arguments.of(
                        """
                                {
                                  "type": "save",
                                  "uuid": "e52232e1-0ded-4587-999f-4dd135a4a94f",
                                  "body": {
                                    "id": 255
                                  }
                                }""",
                        new SaveConsumedReplication<>(
                                fromString("e52232e1-0ded-4587-999f-4dd135a4a94f"),
                                new TestEntity(255L)
                        )
                ),
                Arguments.of(
                        """
                                {
                                  "type": "delete",
                                  "uuid": "e52232e2-0ded-4587-999f-4dd135a4a95f",
                                  "entityId": 255
                                }""",
                        new DeleteConsumedReplication<>(
                                fromString("e52232e2-0ded-4587-999f-4dd135a4a95f"),
                                255L
                        )
                )
        );
    }

    private ReplicationDeserializer<Long, TestEntity> createDeserializer() {
        return new ReplicationDeserializer<>(
                new ObjectMapper(),
                new TypeReference<>() {
                }
        );
    }
}
