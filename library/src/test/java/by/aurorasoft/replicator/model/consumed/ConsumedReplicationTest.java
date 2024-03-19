package by.aurorasoft.replicator.model.consumed;

import by.aurorasoft.replicator.base.entity.TestEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ConsumedReplicationTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @ParameterizedTest
    @MethodSource("provideJsonAndExpectedReplication")
    public void replicationShouldBeDeserializedFromJson(final String givenJson,
                                                        final ConsumedReplication<Long, TestEntity> expected) {
        final ConsumedReplication<Long, TestEntity> actual = deserialize(givenJson);
        assertEquals(expected, actual);
    }

    private ConsumedReplication<Long, TestEntity> deserialize(final String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (final JsonProcessingException cause) {
            throw new RuntimeException(cause);
        }
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
}
