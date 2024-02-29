package by.aurorasoft.replicator.model;

import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;

import static by.aurorasoft.replicator.model.ReplicationType.SAVE;

public final class TransportableReplicationTest extends AbstractSpringBootTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void replicationShouldBeConvertedToJson()
            throws Exception {
        final TransportableReplication givenReplication = new TransportableReplication(SAVE, "{\"id\":255}");

        final String actual = objectMapper.writeValueAsString(givenReplication);
        final String expected = """
                {
                  "type": "SAVE",
                  "dtoJson": "{\\"id\\":255}"
                }""";
        JSONAssert.assertEquals(expected, actual, true);
    }

    @Test
    public void jsonShouldBeConvertedToReplication()
            throws Exception {
        final String givenJson = """
                {
                  "type": "SAVE",
                  "dtoJson": "{\\"id\\":255}"
                }""";

        final TransportableReplication actual = objectMapper.readValue(givenJson, TransportableReplication.class);
        final TransportableReplication expected = new TransportableReplication(SAVE, "{\"id\":255}");
        Assert.assertEquals(expected, actual);
    }
}
