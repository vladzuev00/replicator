package by.aurorasoft.replicator.model.replication.produced;

import by.aurorasoft.replicator.model.view.EntityJsonView;
import by.aurorasoft.replicator.testentity.TestEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;


public final class SaveProducedReplicationTest {

    @Test
    public void entityIdShouldBeGot() {
        Long givenId = 255L;
        TestEntity givenEntity = TestEntity.builder()
                .id(givenId)
                .build();
        EntityJsonView<?> givenEntityJsonView = new EntityJsonView<>(givenEntity);
        SaveProducedReplication givenReplication = new SaveProducedReplication(null);

        Object actual = givenReplication.getEntityIdInternal(givenEntityJsonView);
        assertSame(givenId, actual);
    }
}
