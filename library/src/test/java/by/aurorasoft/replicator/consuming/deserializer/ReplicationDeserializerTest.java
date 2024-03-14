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
                                   "body": {
                                     "id": 255
                                   }
                                }""",
                        new SaveConsumedReplication<>(new TestEntity(255L))
                ),
                Arguments.of(
                        """
                                {
                                   "type": "delete",
                                   "entityId": 255
                                }""",
                        new DeleteConsumedReplication<>(255L)
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
