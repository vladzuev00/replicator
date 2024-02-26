package by.aurorasoft.replicator.model.replication;

import by.aurorasoft.replicator.model.ReplicationType;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import lombok.Value;
import org.junit.Test;

import static by.aurorasoft.replicator.model.ReplicationType.SAVE;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public final class SaveReplicationTest {

    @Test
    public void typeShouldBeGot() {
        final Replication<Long, TestDto> givenReplication = new SaveReplication<>(null);

        final ReplicationType actual = givenReplication.getType();
        assertSame(SAVE, actual);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void replicationShouldBeExecuted() {
        final Replication<Long, TestDto> givenReplication = new SaveReplication<>(null);

        final TestDto givenDto = new TestDto(255L);

        final AbsServiceCRUD<Long, ?, TestDto, ?> givenService = mock(AbsServiceCRUD.class);

        givenReplication.execute(givenService, givenDto);

        verify(givenService, times(1)).save(same(givenDto));
    }

    @Value
    private static class TestDto implements AbstractDto<Long> {
        Long id;
    }
}
