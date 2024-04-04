package by.aurorasoft.replicator.model.consumed;

import by.aurorasoft.replicator.base.entity.TestEntity;
import org.junit.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public final class DeleteConsumedReplicationTest {

    @Test
    @SuppressWarnings("unchecked")
    public void replicationShouldBeExecutedInternally() {
        final Long givenEntityId = 255L;
        final var givenReplication = new DeleteConsumedReplication<Long, TestEntity>(givenEntityId);

        final JpaRepository<TestEntity, Long> givenRepository = mock(JpaRepository.class);

        givenReplication.execute(givenRepository);

        verify(givenRepository, times(1)).deleteById(same(givenEntityId));
    }
}
