package by.aurorasoft.replicator.testutil;

import by.aurorasoft.replicator.annotation.service.ReplicatedService.DtoViewConfig;
import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.Assertions;

import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UtilityClass
public final class DtoViewConfigUtil {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static DtoViewConfig createDtoViewConfig(Class type, String[] includedFields, String[] excludedFields) {
        DtoViewConfig config = mock(DtoViewConfig.class);
        when(config.type()).thenReturn(type);
        when(config.includedFields()).thenReturn(includedFields);
        when(config.excludedFields()).thenReturn(excludedFields);
        return config;
    }

    public static void assertEquals(DtoViewConfig expected, DtoViewConfig actual) {
        assertSame(expected.type(), actual.type());
        assertArrayEquals(expected.includedFields(), actual.includedFields());
        assertArrayEquals(expected.excludedFields(), actual.excludedFields());
    }

    public static void assertEquals(DtoViewConfig[] expected, DtoViewConfig[] actual) {
        Assertions.assertEquals(expected.length, actual.length);
        range(0, expected.length).forEach(i -> assertEquals(expected[i], actual[i]));
    }
}
