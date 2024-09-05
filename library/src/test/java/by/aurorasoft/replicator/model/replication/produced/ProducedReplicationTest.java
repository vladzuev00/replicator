package by.aurorasoft.replicator.model.replication.produced;

import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.aurorasoft.replicator.model.view.DtoJsonView;
import by.aurorasoft.replicator.testcrud.TestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertSame;
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

    @Test
    public void dtoIdShouldBeGot() {
        Object givenBody = new Object();
        TestProducedReplication givenReplication = new TestProducedReplication(givenBody);

        Object actual = givenReplication.getDtoId();
        assertSame(givenBody, actual);
    }

    private static Stream<Arguments> provideReplicationAndExpectedJson() {
        return Stream.of(
                Arguments.of(
                        new SaveProducedReplication(
                                new DtoJsonView<>(
                                        new TestDto(255L, "first-value", "second-value")
                                )
                        ),
                        """
                                {
                                  "type": "save",
                                  "body": {
                                    "id": 255,
                                    "firstProperty": "first-value",
                                    "secondProperty": "second-value"
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

    private static final class TestProducedReplication extends ProducedReplication<Object> {

        public TestProducedReplication(Object body) {
            super(body);
        }

        @Override
        protected Object getDtoIdInternal(Object body) {
            return body;
        }
    }
}
