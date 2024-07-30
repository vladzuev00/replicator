package by.aurorasoft.replicator.model.replication.produced;

import by.aurorasoft.replicator.model.view.DtoJsonView;
import by.aurorasoft.replicator.v2.dto.TestV2Dto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;


public final class SaveProducedReplicationTest {

    @Test
    public void entityIdShouldBeGot() {
        Long givenId = 255L;
        DtoJsonView<?> givenDtoJsonView = new DtoJsonView<>(new TestV2Dto(givenId));
        SaveProducedReplication givenReplication = new SaveProducedReplication(null);

        Object actual = givenReplication.getEntityId(givenDtoJsonView);
        assertSame(givenId, actual);
    }
}
