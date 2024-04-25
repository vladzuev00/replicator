package by.aurorasoft.replicator.model.replication.produced;

import by.aurorasoft.replicator.base.v2.dto.TestDto;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public final class SaveProducedReplicationTest {

    @Test
    public void entityIdShouldBeGot() {
        final Long givenId = 255L;
        final SaveProducedReplication givenReplication = new SaveProducedReplication(new TestDto(givenId));

        final Object actual = givenReplication.getEntityId();
        assertSame(givenId, actual);
    }
}
