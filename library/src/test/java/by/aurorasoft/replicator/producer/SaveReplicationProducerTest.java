package by.aurorasoft.replicator.producer;

import by.aurorasoft.replicator.model.setting.ReplicationProducerSetting.EntityViewSetting;
import by.aurorasoft.replicator.model.view.EntityJsonView;
import lombok.Value;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class SaveReplicationProducerTest {
    private final EntityViewSetting[] givenEntityViewSettings = new EntityViewSetting[]{
            new EntityViewSetting(TestEntity.class, new String[]{"first-property"})
    };
    private final SaveReplicationProducer producer = new SaveReplicationProducer(null, null, givenEntityViewSettings);

    @Test
    public void entityIdShouldBeGot() {
        Long givenEntityId = 255L;
        TestEntity givenEntity = new TestEntity(givenEntityId, "first-value", "second-value");

        Object actual = producer.getEntityId(givenEntity);
        assertSame(givenEntityId, actual);
    }

    @Test
    public void replicationBodyShouldBeCreated() {
        TestEntity givenEntity = new TestEntity(255L, "first-value", "second-value");

        EntityJsonView<?> actual = producer.createReplicationBody(givenEntity);
        EntityJsonView<?> expected = new EntityJsonView<>();
    }

    @Value
    public static class TestEntity {
        Long id;
        String firstProperty;
        String secondProperty;
    }
}
