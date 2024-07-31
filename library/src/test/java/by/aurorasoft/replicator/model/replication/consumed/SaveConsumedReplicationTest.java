package by.aurorasoft.replicator.model.replication.consumed;

import by.aurorasoft.replicator.testentity.TestEntity;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.mockito.Mockito.*;

public final class SaveConsumedReplicationTest {

    @Test
    public void replicationShouldBeExecutedInternally() {
        TestEntity givenEntity = TestEntity.builder().build();
        SaveConsumedReplication<TestEntity, Long> givenReplication = new SaveConsumedReplication<>(givenEntity);
        @SuppressWarnings("unchecked") JpaRepository<TestEntity, Long> givenRepository = mock(JpaRepository.class);

        givenReplication.executeInternal(givenRepository);

        verify(givenRepository, times(1)).save(same(givenEntity));
    }
}
