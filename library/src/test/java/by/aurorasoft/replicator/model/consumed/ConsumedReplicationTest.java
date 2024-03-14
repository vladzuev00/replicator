package by.aurorasoft.replicator.model.consumed;

import by.aurorasoft.replicator.base.entity.TestEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static by.aurorasoft.replicator.util.ReplicationAssertUtil.assertDelete;
import static by.aurorasoft.replicator.util.ReplicationAssertUtil.assertSave;
import static java.util.UUID.fromString;

public final class ConsumedReplicationTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

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
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (final JsonProcessingException cause) {
            throw new RuntimeException(cause);
        }
    }
}
