package by.aurorasoft.replicator.model.replication.produced;

import by.aurorasoft.replicator.model.view.DtoJsonView;
import by.aurorasoft.replicator.testcrud.TestDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class SaveProducedReplicationTest {

    @Test
    public void dtoIdShouldBeGotInternally() {
        Long givenDtoId = 255L;
        TestDto givenDto = TestDto.builder()
                .id(givenDtoId)
                .build();
        DtoJsonView<?> givenDtoJsonView = new DtoJsonView<>(givenDto);
        SaveProducedReplication givenReplication = new SaveProducedReplication(givenDtoJsonView);

        Object actual = givenReplication.getDtoIdInternal(givenDtoJsonView);
        assertSame(givenDtoId, actual);
    }
}
