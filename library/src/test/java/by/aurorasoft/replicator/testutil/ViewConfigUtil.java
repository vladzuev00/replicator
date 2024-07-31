package by.aurorasoft.replicator.testutil;

import by.aurorasoft.replicator.annotation.ReplicatedRepository.View;
import lombok.experimental.UtilityClass;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UtilityClass
public final class ViewConfigUtil {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static View createViewConfig(Class type, String[] includedFields, String[] excludedFields) {
        View config = mock(View.class);
        when(config.type()).thenReturn(type);
        when(config.excludedFields()).thenReturn(excludedFields);
        return config;
    }

    public static void checkEquals(View expected, View actual) {
        assertSame(expected.type(), actual.type());
        assertArrayEquals(expected.excludedFields(), actual.excludedFields());
    }
}
