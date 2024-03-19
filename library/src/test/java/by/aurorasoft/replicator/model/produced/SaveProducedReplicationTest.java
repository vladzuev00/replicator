package by.aurorasoft.replicator.model.produced;

import by.aurorasoft.replicator.base.dto.TestDto;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public final class SaveProducedReplicationTest {

    @Test
    public void entityIdShouldBeGot() {
        final Long givenId = 255L;
        final var givenReplication = new SaveProducedReplication<>(new TestDto(givenId));

        final Long actual = givenReplication.getEntityId();
        assertSame(givenId, actual);
    }
}
