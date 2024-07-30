package by.aurorasoft.replicator.testutil;

import by.aurorasoft.replicator.annotation.ReplicatedService.ViewConfig;
import lombok.experimental.UtilityClass;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UtilityClass
public final class ViewConfigUtil {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static ViewConfig createViewConfig(Class type, String[] includedFields, String[] excludedFields) {
        ViewConfig config = mock(ViewConfig.class);
        when(config.type()).thenReturn(type);
        when(config.includedFields()).thenReturn(includedFields);
        when(config.excludedFields()).thenReturn(excludedFields);
        return config;
    }
}
