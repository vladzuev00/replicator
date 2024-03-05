package by.aurorasoft.replicator.model;

import by.aurorasoft.replicator.base.dto.TestDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.stream.Stream;


public final class ReplicationTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @ParameterizedTest
    @MethodSource("provideReplicationAndExpectedJson")
    public void replicationShouldBeSerializedToJson(final Replication<Long, TestDto> givenReplication,
                                                    final String expected)
            throws Exception {
        final String actual = objectMapper.writeValueAsString(givenReplication);
        JSONAssert.assertEquals(expected, actual, true);
    }

    @ParameterizedTest
    @MethodSource("provideJsonAndExpectedReplication")
    public void replicationShouldBeDeserializedFromJson(final String givenJson,
                                                        final Replication<Long, TestDto> expected)
            throws Exception {
        final Replication<Long, TestDto> actual = objectMapper.readValue(givenJson, new TypeReference<>() {
        });
        Assertions.assertEquals(expected, actual);
    }

    private static Stream<Arguments> provideReplicationAndExpectedJson() {
        return Stream.of(
                Arguments.of(
                        new SaveReplication<>(new TestDto(255L)),
                        """
                                {
                                   "@type": "SaveReplication",
                                   "dto": {
                                     "id": 255
                                   }
                                }"""
                ),
                Arguments.of(
                        new UpdateReplication<>(new TestDto(255L)),
                        """
                                {
                                   "@type": "UpdateReplication",
                                   "dto": {
                                     "id": 255
                                   }
                                }"""
                ),
                Arguments.of(
                        new DeleteReplication<>(255L),
                        """
                                {
                                   "@type": "DeleteReplication",
                                   "entityId": 255
                                }"""
                )
        );
    }

    private static Stream<Arguments> provideJsonAndExpectedReplication() {
        return Stream.of(
                Arguments.of(
                        """
                                {
                                   "@type": "SaveReplication",
                                   "dto": {
                                     "id": 255
                                   }
                                }""",
                        new SaveReplication<>(new TestDto(255L))
                ),
                Arguments.of(
                        """
                                {
                                   "@type": "UpdateReplication",
                                   "dto": {
                                     "id": 255
                                   }
                                }""",
                        new UpdateReplication<>(new TestDto(255L))
                ),
                Arguments.of(
                        """
                                {
                                   "@type": "DeleteReplication",
                                   "entityId": 255
                                }""",
                        new DeleteReplication<>(255L)
                )
        );
    }
}
