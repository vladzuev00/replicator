package by.aurorasoft.replicator.model.view;

import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.aurorasoft.replicator.testcrud.TestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public final class DtoJsonViewTest extends AbstractSpringBootTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void viewShouldBeSerializedToJson()
            throws Exception {
        var givenView = new DtoJsonView<>(new TestDto(255L, "first-value", "second-value"));

        String actual = objectMapper.writeValueAsString(givenView);
        String expected = """
                {
                  "id": 255,
                  "firstProperty": "first-value",
                  "secondProperty": "second-value"
                }""";
        assertEquals(expected, actual, true);
    }

    @Test
    public void dtoShouldBeGot() {
        TestDto givenDto = TestDto.builder().build();
        DtoJsonView<?> givenJsonView = new DtoJsonView<>(givenDto);

        Object actual = givenJsonView.getDto();
        assertSame(givenDto, actual);
    }
}
