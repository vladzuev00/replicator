package by.aurorasoft.replicator.model.view;

import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.aurorasoft.replicator.testcrud.TestEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public final class EntityJsonViewTest extends AbstractSpringBootTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void viewShouldBeSerializedToJson()
            throws Exception {
        var givenView = new EntityJsonView<>(new TestEntity(255L, "first-value", "second-value"));

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
    public void entityShouldBeGot() {
        TestEntity givenEntity = new TestEntity();
        EntityJsonView<?> givenJsonView = new EntityJsonView<>(givenEntity);

        Object actual = givenJsonView.getEntity();
        assertSame(givenEntity, actual);
    }
}
