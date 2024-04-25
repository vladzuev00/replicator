package by.aurorasoft.replicator.model.replication.produced;

import by.aurorasoft.replicator.base.v2.dto.TestV2Dto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public final class ProducedReplicationTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @ParameterizedTest
    @MethodSource("provideReplicationAndExpectedJson")
    public void replicationShouldBeSerializedToJson(final ProducedReplication givenReplication, final String expected)
            throws Exception {
        final String actual = objectMapper.writeValueAsString(givenReplication);
        assertEquals(expected, actual, true);
    }

    private static Stream<Arguments> provideReplicationAndExpectedJson() {
        return Stream.of(
                Arguments.of(
                        new SaveProducedReplication(new TestV2Dto(255L)),
                        """
                                {
                                  "type": "save",
                                  "body": {
                                    "id": 255
                                  }
                                }"""
                ),
                Arguments.of(
                        new DeleteProducedReplication(255L),
                        """
                                {
                                  "type": "delete",
                                  "body": 255
                                }"""
                )
        );
    }
}
