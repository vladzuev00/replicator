package by.aurorasoft.replicator.model;

import by.aurorasoft.replicator.base.dto.TestDto;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public final class UpdateReplicationTest {

    @Test
    public void entityIdShouldBeGot() {
        final Long givenEntityId = 255L;
        final TestDto givenDto = new TestDto(givenEntityId);
        final Replication<Long, TestDto> givenReplication = new UpdateReplication<>(givenDto);

        final Long actual = givenReplication.getEntityId();
        assertSame(givenEntityId, actual);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void replicationShouldBeExecuted() {
        final TestDto givenDto = new TestDto(255L);
        final Replication<Long, TestDto> givenReplication = new UpdateReplication<>(givenDto);

        final AbsServiceCRUD<Long, ?, TestDto, ?> givenService = mock(AbsServiceCRUD.class);

        givenReplication.execute(givenService);

        verify(givenService, times(1)).update(same(givenDto));
    }
}
