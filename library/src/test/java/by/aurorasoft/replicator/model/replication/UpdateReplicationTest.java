package by.aurorasoft.replicator.model.replication;

import by.aurorasoft.replicator.base.dto.TestDto;
import by.aurorasoft.replicator.model.ReplicationType;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import org.junit.Test;

import static by.aurorasoft.replicator.model.ReplicationType.UPDATE;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public final class UpdateReplicationTest {

    @Test
    public void typeShouldBeGot() {
        final Replication<Long, TestDto> givenReplication = new UpdateReplication<>(null);

        final ReplicationType actual = givenReplication.getType();
        assertSame(UPDATE, actual);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void replicationShouldBeExecuted() {
        final Replication<Long, TestDto> givenReplication = new UpdateReplication<>(null);

        final TestDto givenDto = new TestDto(255L);

        final AbsServiceCRUD<Long, ?, TestDto, ?> givenService = mock(AbsServiceCRUD.class);

        givenReplication.execute(givenService, givenDto);

        verify(givenService, times(1)).update(same(givenDto));
    }
}
