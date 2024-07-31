package by.aurorasoft.replicator.model.view;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class EntityJsonViewTest {

    @Test
    public void entityShouldBeGot() {
        Object givenEntity = new Object();
        EntityJsonView<?> givenJsonView = new EntityJsonView<>(givenEntity);

        Object actual = givenJsonView.getEntity();
        assertSame(givenEntity, actual);
    }
}
