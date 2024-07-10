package by.aurorasoft.replicator.model.view;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class DtoJsonViewTest {

    @Test
    public void dtoShouldBeGot() {
        Object givenDto = new Object();
        DtoJsonView<?> givenView = new DtoJsonView<>(givenDto);

        Object actual = givenView.getDto();
        assertSame(givenDto, actual);
    }
}
