package by.aurorasoft.replicator.annotation.processing.processor.operation;

import org.junit.jupiter.api.Test;

import javax.lang.model.type.TypeMirror;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public final class ReplicatedDeleteByIdsProcessorTest {
    private final ReplicatedDeleteByIdsProcessor processor = new ReplicatedDeleteByIdsProcessor();

    @Test
    public void replicatedServiceShouldBeValid() {
        TypeMirror givenMirror = mock(TypeMirror.class);

        boolean actual = processor.isValidReplicatedService(givenMirror);
        assertTrue(actual);
    }

    @Test
    public void returnTypeShouldBeValid() {
        throw new UnsupportedOperationException();
    }
}
