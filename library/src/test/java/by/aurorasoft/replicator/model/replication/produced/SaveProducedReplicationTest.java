package by.aurorasoft.replicator.model.replication.produced;

import by.aurorasoft.replicator.model.view.EntityJsonView;
import by.aurorasoft.replicator.testcrud.TestEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class SaveProducedReplicationTest {

    @Test
    public void entityIdShouldBeGotInternally() {
        Long givenEntityId = 255L;
        TestEntity givenEntity = TestEntity.builder()
                .id(givenEntityId)
                .build();
        EntityJsonView<?> givenEntityJsonView = new EntityJsonView<>(givenEntity);
        SaveProducedReplication givenReplication = new SaveProducedReplication(givenEntityJsonView);

        Object actual = givenReplication.getEntityIdInternal(givenEntityJsonView);
        assertSame(givenEntityId, actual);
    }
}
