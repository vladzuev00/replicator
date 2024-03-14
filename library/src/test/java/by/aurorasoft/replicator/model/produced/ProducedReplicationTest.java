package by.aurorasoft.replicator.model.produced;

import by.aurorasoft.replicator.base.dto.TestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static java.util.UUID.fromString;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public final class ProducedReplicationTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @ParameterizedTest
    @MethodSource("provideReplicationAndExpectedJson")
    public void replicationShouldBeSerializedToJson(final ProducedReplication<Long> givenReplication,
                                                    final String expected)
            throws Exception {
        final String actual = objectMapper.writeValueAsString(givenReplication);
        assertEquals(expected, actual, true);
    }

    private static Stream<Arguments> provideReplicationAndExpectedJson() {
        return Stream.of(
                Arguments.of(
                        new SaveProducedReplication<>(
                                fromString("e52232e1-0ded-4587-999f-4dd135a4a94f"),
                                new TestDto(255L)
                        ),
                        """
                                {
                                  "type": "save",
                                  "uuid": "e52232e1-0ded-4587-999f-4dd135a4a94f",
                                  "body": {
                                    "id": 255
                                  }
                                }"""
                ),
                Arguments.of(
                        new DeleteProducedReplication<>(
                                fromString("e52232e2-0ded-4587-999f-4dd135a4a95f"),
                                255L
                        ),
                        """
                                {
                                  "type": "delete",
                                  "uuid": "e52232e2-0ded-4587-999f-4dd135a4a95f",
                                  "entityId": 255
                                }"""
                )
        );
    }
}
