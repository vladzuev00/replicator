package by.aurorasoft.replicator.producer;

import by.aurorasoft.replicator.model.setting.ReplicationProducerSetting.EntityViewSetting;
import by.aurorasoft.replicator.model.view.EntityJsonView;
import by.aurorasoft.replicator.testentity.TestEntity;
import com.monitorjbl.json.Match;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static by.aurorasoft.replicator.testutil.ReflectionUtil.getFieldValue;
import static com.monitorjbl.json.Match.match;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class SaveReplicationProducerTest {
    private static final String IGNORED_FIELD_NAME = "firstProperty";
    private static final String FIELD_NAME_MATCHES_BY_TYPES = "matches";

    private final EntityViewSetting[] givenEntityViewSettings = new EntityViewSetting[]{
            new EntityViewSetting(TestEntity.class, new String[]{IGNORED_FIELD_NAME})
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

        Object actualEntity = actual.getEntity();
        assertSame(givenEntity, actualEntity);

        Map<Class<?>, Match> actualMatchesByTypes = getMatchesByTypes(actual);
        Map<Class<?>, Match> expectedMatchesByTypes = Map.of(
                TestEntity.class, match().exclude("firstProperty")
        );
        assertEquals(expectedMatchesByTypes, actualMatchesByTypes);
    }

    @SuppressWarnings("unchecked")
    private Map<Class<?>, Match> getMatchesByTypes(EntityJsonView<?> view) {
        return getFieldValue(view, FIELD_NAME_MATCHES_BY_TYPES, Map.class);
    }
}
