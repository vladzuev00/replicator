package by.aurorasoft.replicator.consuming.serde;

import by.aurorasoft.replicator.base.v2.entity.TestV2Entity;
import by.aurorasoft.replicator.model.replication.consumed.ConsumedReplication;
import by.aurorasoft.replicator.model.replication.consumed.DeleteConsumedReplication;
import by.aurorasoft.replicator.model.replication.consumed.SaveConsumedReplication;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public final class ReplicationSerdeTest {
    private static final String GIVEN_TOPIC_NAME = "topic";

    private final ReplicationSerde<Long, TestV2Entity> serde = createSerde();

    @Test(expected = UnsupportedOperationException.class)
    public void serializerShouldNotBeCreated() {
        serde.serializer();
    }

    @ParameterizedTest
    @MethodSource("provideJsonAndExpectedReplication")
    public void replicationShouldBeDeserialized(final String givenJson,
                                                final ConsumedReplication<Long, TestV2Entity> expected) {
        final ConsumedReplication<Long, TestV2Entity> actual = deserialize(givenJson);
        assertEquals(expected, actual);
    }

    private static ReplicationSerde<Long, TestV2Entity> createSerde() {
        return new ReplicationSerde<>(new TypeReference<>() {
        });
    }

    private ConsumedReplication<Long, TestV2Entity> deserialize(final String json) {
        return serde.deserializer().deserialize(GIVEN_TOPIC_NAME, json.getBytes());
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
                        new SaveConsumedReplication<>(new TestV2Entity(255L))
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
