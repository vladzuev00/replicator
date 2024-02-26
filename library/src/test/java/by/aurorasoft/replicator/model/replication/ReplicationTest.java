package by.aurorasoft.replicator.model.replication;

import by.aurorasoft.replicator.model.ReplicationType;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import lombok.Value;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public final class ReplicationTest {

    @Test
    public void entityIdShouldBeGot() {
        final Long givenId = 255L;
        final TestDto givenDto = new TestDto(givenId);
        final TestReplication givenReplication = new TestReplication(givenDto);

        final Long actual = givenReplication.getEntityId();
        assertSame(givenId, actual);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void replicationShouldBeExecuted() {
        final TestDto givenDto = new TestDto(255L);
        final TestReplication givenReplication = new TestReplication(givenDto);

        final AbsServiceCRUD<Long, ?, TestDto, ?> givenService = mock(AbsServiceCRUD.class);

        givenReplication.execute(givenService);

        verify(givenService, times(1)).save(same(givenDto));
    }

    @Value
    private static class TestDto implements AbstractDto<Long> {
        Long id;
    }

    private static final class TestReplication extends Replication<Long, TestDto> {

        public TestReplication(final TestDto dto) {
            super(dto);
        }

        @Override
        public ReplicationType getType() {
            throw new UnsupportedOperationException();
        }

        @Override
        protected void execute(final AbsServiceCRUD<Long, ?, TestDto, ?> service, final TestDto dto) {
            service.save(dto);
        }
    }
}
