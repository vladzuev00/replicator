package by.aurorasoft.replicator.model.replication.produced;

import by.aurorasoft.replicator.base.v2.dto.TestV2Dto;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public final class SaveProducedReplicationTest {

    @Test
    public void entityIdShouldBeGot() {
        final Long givenId = 255L;
        final Object givenDto = new TestV2Dto(givenId);
        final SaveProducedReplication givenReplication = new SaveProducedReplication(null);

        final Object actual = givenReplication.getEntityId(givenDto);
        assertSame(givenId, actual);
    }
}
