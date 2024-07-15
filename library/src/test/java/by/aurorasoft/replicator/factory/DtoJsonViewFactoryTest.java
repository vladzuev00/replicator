package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedService.ViewConfig;
import by.aurorasoft.replicator.model.view.DtoJsonView;
import lombok.Value;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class DtoJsonViewFactoryTest {
    private final DtoJsonViewFactory factory = new DtoJsonViewFactory();

    @Test
    public void viewShouldBeCreated() {
        TestDto givenDto = new TestDto(255L, "dto-name");
        ViewConfig[] givenConfigs = new ViewConfig[]{
                createConfig(TestDto.class, new String[]{"id"}, new String[]{"name"}),
                createConfig(Object.class, new String[]{"first-field"}, new String[]{"second-field", "third-field"})
        };

        DtoJsonView<TestDto> actual = factory.create(givenDto, givenConfigs);

        TestDto actualDto = actual.getDto();
        assertSame(givenDto, actualDto);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private ViewConfig createConfig(Class type, String[] includedFields, String[] excludedFields) {
        ViewConfig config = mock(ViewConfig.class);
        when(config.type()).thenReturn(type);
        when(config.includedFields()).thenReturn(includedFields);
        when(config.excludedFields()).thenReturn(excludedFields);
        return config;
    }

    @Value
    private static class TestDto {
        Long id;
        String name;
    }
}
