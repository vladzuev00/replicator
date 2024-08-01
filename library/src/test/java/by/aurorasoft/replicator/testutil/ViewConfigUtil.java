package by.aurorasoft.replicator.testutil;

import by.aurorasoft.replicator.annotation.ReplicatedRepository.EntityView;
import lombok.experimental.UtilityClass;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UtilityClass
public final class ViewConfigUtil {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static EntityView createViewConfig(Class type, String[] excludedFields) {
        EntityView config = mock(EntityView.class);
        when(config.type()).thenReturn(type);
        when(config.excludedFields()).thenReturn(excludedFields);
        return config;
    }

    public static void checkEquals(EntityView expected, EntityView actual) {
        assertSame(expected.type(), actual.type());
        assertArrayEquals(expected.excludedFields(), actual.excludedFields());
    }
}
