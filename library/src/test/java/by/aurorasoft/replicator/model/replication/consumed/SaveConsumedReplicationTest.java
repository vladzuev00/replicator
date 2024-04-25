package by.aurorasoft.replicator.model.replication.consumed;

import by.aurorasoft.replicator.base.v2.entity.TestEntity;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.mockito.Mockito.*;

public final class SaveConsumedReplicationTest {

    @Test
    @SuppressWarnings("unchecked")
    public void replicationShouldBeExecutedInternally() {
        final TestEntity givenEntity = new TestEntity(255L);
        final SaveConsumedReplication<TestEntity, Long> givenReplication = new SaveConsumedReplication<>(givenEntity);

        final JpaRepository<TestEntity, Long> givenRepository = mock(JpaRepository.class);

        givenReplication.executeInternal(givenRepository);

        verify(givenRepository, times(1)).save(same(givenEntity));
    }
}
