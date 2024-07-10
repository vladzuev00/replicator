package by.aurorasoft.replicator.model.replication.produced;

import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.aurorasoft.replicator.model.view.DtoJsonView;
import by.aurorasoft.replicator.v2.dto.TestV2Dto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitorjbl.json.JsonViewModule;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public final class ProducedReplicationTest extends AbstractSpringBootTest {

    @Autowired
    private ObjectMapper objectMapper;

    @ParameterizedTest
    @MethodSource("provideReplicationAndExpectedJson")
    public void replicationShouldBeSerializedToJson(ProducedReplication<?> givenReplication, String expected)
            throws Exception {
        String actual = objectMapper.writeValueAsString(givenReplication);
        assertEquals(expected, actual, true);
    }

    private static Stream<Arguments> provideReplicationAndExpectedJson() {
        return Stream.of(
                Arguments.of(
                        new SaveProducedReplication(new DtoJsonView<>(new TestV2Dto(255L))),
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
