package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedService.ViewConfig;
import by.aurorasoft.replicator.model.view.DtoJsonView;
import lombok.Value;
import org.junit.jupiter.api.Test;

import static by.aurorasoft.replicator.testutil.ViewConfigUtil.createViewConfig;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class DtoJsonViewFactoryTest {
    private final DtoJsonViewFactory factory = new DtoJsonViewFactory();

    @Test
    public void viewShouldBeCreated() {
        TestDto givenDto = new TestDto(255L, "dto-name");
        ViewConfig[] givenConfigs = new ViewConfig[]{
                createViewConfig(
                        TestDto.class,
                        new String[]{"id"},
                        new String[]{"name"}
                ),
                createViewConfig(
                        Object.class,
                        new String[]{"first-field"},
                        new String[]{"second-field", "third-field"}
                )
        };

        DtoJsonView<TestDto> actual = factory.create(givenDto, givenConfigs);

        TestDto actualDto = actual.getDto();
        assertSame(givenDto, actualDto);
    }

    @Value
    private static class TestDto {
        Long id;
        String name;
    }
}
