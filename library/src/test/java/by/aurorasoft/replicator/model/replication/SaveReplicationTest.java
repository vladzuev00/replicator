package by.aurorasoft.replicator.model.replication;

import by.aurorasoft.replicator.base.dto.TestDto;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public final class SaveReplicationTest {

    @Test
    public void entityIdShouldBeGot() {
        final Long givenId = 255L;
        final TestDto givenDto = new TestDto(givenId);
        final Replication<Long, TestDto> givenReplication = new SaveReplication<>(givenDto);

        final Long actual = givenReplication.getEntityId();
        assertSame(givenId, actual);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void replicationShouldBeExecuted() {
        final TestDto givenDto = new TestDto(255L);
        final Replication<Long, TestDto> givenReplication = new SaveReplication<>(givenDto);

        final AbsServiceCRUD<Long, ?, TestDto, ?> givenService = mock(AbsServiceCRUD.class);

        givenReplication.execute(givenService);

        verify(givenService, times(1)).save(same(givenDto));
    }
}
