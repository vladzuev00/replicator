package by.aurorasoft.replicator.consuming.deserializer;

import by.aurorasoft.replicator.base.entity.TestEntity;
import by.aurorasoft.replicator.model.consumed.ConsumedReplication;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static by.aurorasoft.replicator.util.ReplicationAssertUtil.assertDelete;
import static by.aurorasoft.replicator.util.ReplicationAssertUtil.assertSave;
import static java.util.UUID.fromString;

public final class ReplicationDeserializerTest {
    private static final String GIVEN_TOPIC_NAME = "topic";

    private final ReplicationDeserializer<Long, TestEntity> deserializer = createDeserializer();

    @Test
    public void saveReplicationShouldBeDeserializedFromJson() {
        final String givenJson = """
                {
                  "type": "save",
                  "uuid": "e52232e1-0ded-4587-999f-4dd135a4a94f",
                  "body": {
                    "id": 255
                  }
                }""";

        final ConsumedReplication<Long, TestEntity> actual = deserialize(givenJson);
        assertSave(actual, fromString("e52232e1-0ded-4587-999f-4dd135a4a94f"), new TestEntity(255L));
    }

    @Test
    public void deleteReplicationShouldBeDeserializedFromJson() {
        final String givenJson = """
                {
                  "type": "delete",
                  "uuid": "e52232e2-0ded-4587-999f-4dd135a4a95f",
                  "entityId": 255
                }""";

        final ConsumedReplication<Long, TestEntity> actual = deserialize(givenJson);
        assertDelete(actual, fromString("e52232e2-0ded-4587-999f-4dd135a4a95f"), 255L);
    }

    private ConsumedReplication<Long, TestEntity> deserialize(final String json) {
        return deserializer.deserialize(GIVEN_TOPIC_NAME, json.getBytes());
    }

    private ReplicationDeserializer<Long, TestEntity> createDeserializer() {
        return new ReplicationDeserializer<>(
                new ObjectMapper(),
                new TypeReference<>() {
                }
        );
    }
}
