package by.aurorasoft.replicator.model.replication.produced;

import by.aurorasoft.replicator.model.view.EntityJsonView;
import lombok.Value;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class SaveProducedReplicationTest {

    @Test
    public void entityIdShouldBeGotInternally() {
        Long givenId = 255L;
        TestEntity givenEntity = new TestEntity(givenId);
        EntityJsonView<?> givenEntityJsonView = new EntityJsonView<>(givenEntity);
        SaveProducedReplication givenReplication = new SaveProducedReplication(null);

        Object actual = givenReplication.getEntityIdInternal(givenEntityJsonView);
        assertSame(givenId, actual);
    }

    @Value
    public static class TestEntity {
        Long id;
    }
}
