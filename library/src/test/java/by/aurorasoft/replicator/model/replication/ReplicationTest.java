package by.aurorasoft.replicator.model.replication;

import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.aurorasoft.replicator.base.dto.TestDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;


public final class ReplicationTest extends AbstractSpringBootTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void saveReplicationShouldBeSerializedToJson()
            throws Exception {
        final Replication<Long, TestDto> givenReplication = new SaveReplication<>(new TestDto(255L));

        final String actual = objectMapper.writeValueAsString(givenReplication);
        final String expected = """
                {
                   "type": "SaveReplication",
                   "dto": {
                     "id": 255
                   }
                }""";
        JSONAssert.assertEquals(expected, actual, true);
    }

    @Test
    public void saveReplicationShouldBeDeserializedFromJson()
            throws Exception {
        final String givenJson = """
                {
                   "type": "SaveReplication",
                   "dto": {
                     "id": 255
                   }
                }""";

        final Replication<Long, TestDto> actual = objectMapper.readValue(givenJson, new TypeReference<>() {
        });
        final Replication<Long, TestDto> expected = new SaveReplication<>(new TestDto(255L));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void updateReplicationShouldBeSerializedToJson()
            throws Exception {
        final Replication<Long, TestDto> givenReplication = new UpdateReplication<>(new TestDto(255L));

        final String actual = objectMapper.writeValueAsString(givenReplication);
        final String expected = """
                {
                  "@class": "by.aurorasoft.replicator.model.replication.UpdateReplication",
                  "type": "UPDATE",
                  "entityId": 255
                }""";
        JSONAssert.assertEquals(expected, actual, true);
    }
}
