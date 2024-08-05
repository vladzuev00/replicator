package by.aurorasoft.replicator.model.view;

import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.aurorasoft.replicator.mapperwrapper.ReplicationObjectMapperWrapper;
import by.aurorasoft.replicator.testentity.TestEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public final class EntityJsonViewTest extends AbstractSpringBootTest {

    @Autowired
    private ReplicationObjectMapperWrapper mapperWrapper;

    @Test
    public void viewShouldBeSerializedToJson()
            throws Exception {
        EntityJsonView<?> givenView = new EntityJsonView<>(new TestEntity(255L, "first-value", "second-value"));

        String actual = mapperWrapper.getMapper().writeValueAsString(givenView);
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
        Object givenEntity = new Object();
        EntityJsonView<?> givenJsonView = new EntityJsonView<>(givenEntity);

        Object actual = givenJsonView.getEntity();
        assertSame(givenEntity, actual);
    }
}
