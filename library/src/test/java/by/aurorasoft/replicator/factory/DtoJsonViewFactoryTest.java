package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedRepository.EntityView;
import by.aurorasoft.replicator.model.view.EntityJsonView;
import lombok.Value;
import org.junit.jupiter.api.Test;

import static by.aurorasoft.replicator.testutil.ViewConfigUtil.createViewConfig;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class DtoJsonViewFactoryTest {
    private final DtoJsonViewFactory factory = new DtoJsonViewFactory();

    @Test
    public void viewShouldBeCreated() {
        TestDto givenDto = new TestDto(255L, "dto-name");
        EntityView[] givenConfigs = new EntityView[]{
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

        EntityJsonView<TestDto> actual = factory.create(givenDto, givenConfigs);

        TestDto actualDto = actual.getEntity();
        assertSame(givenDto, actualDto);
    }

    @Value
    private static class TestDto {
        Long id;
        String name;
    }
}
