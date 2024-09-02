package by.aurorasoft.replicator.factory.replication;

import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
import by.aurorasoft.replicator.model.view.EntityJsonView;
import by.aurorasoft.replicator.testentity.TestEntity;
import org.junit.jupiter.api.Test;

import static com.monitorjbl.json.Match.match;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class SaveProducedReplicationFactoryTest {
    private final SaveProducedReplicationFactory factory = new SaveProducedReplicationFactory();

    @Test
    public void replicationShouldBeCreated() {
        TestEntity givenEntity = new TestEntity();
        String givenExcludedField = "secondProperty";
        EntityViewSetting[] givenViewSettings = {
                new EntityViewSetting(TestEntity.class, new String[]{givenExcludedField})
        };

        SaveProducedReplication actual = factory.create(givenEntity, givenViewSettings);
        EntityJsonView<TestEntity> expectedView = new EntityJsonView<>(givenEntity);
        expectedView.onClass(TestEntity.class, match().exclude(givenExcludedField));
        SaveProducedReplication expected = new SaveProducedReplication(expectedView);
        assertEquals(expected, actual);

        Object actualEntity = actual.getBody().getEntity();
        assertSame(givenEntity, actualEntity);
    }
}
