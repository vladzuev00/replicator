package by.aurorasoft.replicator.model.consumed;

import by.aurorasoft.replicator.base.entity.TestEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static java.util.UUID.fromString;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ConsumedReplicationTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @ParameterizedTest
    @MethodSource("provideJsonAndExpectedReplication")
    public void replicationShouldBeDeserializedFromJson(final String givenJson,
                                                        final ConsumedReplication<Long, TestEntity> expected)
            throws Exception {
        final ConsumedReplication<Long, TestEntity> actual = objectMapper.readValue(givenJson, new TypeReference<>() {
        });
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
}
