package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedRepository.EntityView;
import by.aurorasoft.replicator.model.view.EntityJsonView;
import by.aurorasoft.replicator.testentity.TestEntity;
import com.monitorjbl.json.Match;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static by.aurorasoft.replicator.testutil.ReflectionUtil.getFieldValue;
import static by.aurorasoft.replicator.testutil.ViewConfigUtil.createViewConfig;
import static com.monitorjbl.json.Match.match;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class EntityJsonViewFactoryTest {
    private static final String FIELD_NAME_MATCHES_BY_TYPES = "matches";


    private final EntityJsonViewFactory factory = new EntityJsonViewFactory();

    @Test
    public void viewShouldBeCreated() {
        TestEntity givenEntity = new TestEntity(255L, "first-value", "second-value");
        EntityView[] givenConfigs = new EntityView[]{
                createViewConfig(TestEntity.class, new String[]{"first-field"}),
                createViewConfig(Object.class, new String[]{"second-field", "third-field"})
        };

        EntityJsonView<TestEntity> actual = factory.create(givenEntity, givenConfigs);

        TestEntity actualEntity = actual.getEntity();
        assertSame(givenEntity, actualEntity);

        Map<Class<?>, Match> actualMatchesByTypes = getMatchesByTypes(actual);
        Map<Class<?>, Match> expectedMatchesByTypes = Map.of(
                TestEntity.class, match().exclude("first-field"),
                Object.class, match().exclude("second-field", "third-field")
        );
        assertEquals(expectedMatchesByTypes, actualMatchesByTypes);
    }

    @SuppressWarnings("unchecked")
    private Map<Class<?>, Match> getMatchesByTypes(EntityJsonView<?> view) {
        return getFieldValue(view, FIELD_NAME_MATCHES_BY_TYPES, Map.class);
    }
}
