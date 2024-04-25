package by.aurorasoft.replicator.model.replication.produced;

import by.aurorasoft.replicator.base.v2.dto.TestDto;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public final class SaveProducedReplicationTest {

    @Test
    public void entityIdShouldBeGot() {
        final Long givenId = 255L;
        final Object givenBody = new TestDto(givenId);
        final SaveProducedReplication givenReplication = new SaveProducedReplication(null);

        final Object actual = givenReplication.getEntityId(givenBody);
        assertSame(givenId, actual);
    }
}
