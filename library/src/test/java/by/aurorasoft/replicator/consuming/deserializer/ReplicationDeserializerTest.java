package by.aurorasoft.replicator.consuming.deserializer;

import by.aurorasoft.replicator.base.dto.TestDto;
import by.aurorasoft.replicator.model.DeleteReplication;
import by.aurorasoft.replicator.model.Replication;
import by.aurorasoft.replicator.model.SaveReplication;
import by.aurorasoft.replicator.model.UpdateReplication;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ReplicationDeserializerTest {
    private final ReplicationDeserializer<Long, TestDto> deserializer = createDeserializer();

    @ParameterizedTest
    @MethodSource("provideJsonAndExpectedReplication")
    public void replicationShouldBeDeserialized(final String givenJson, final Replication<Long, TestDto> expected) {
        final String givenTopicName = "topic";
        final byte[] givenJsonBytes = givenJson.getBytes();

        final Replication<Long, TestDto> actual = deserializer.deserialize(givenTopicName, givenJsonBytes);
        assertEquals(expected, actual);
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

    private ReplicationDeserializer<Long, TestDto> createDeserializer() {
        return new ReplicationDeserializer<>(
                new ObjectMapper(),
                new TypeReference<>() {
                }
        );
    }
}
