package by.aurorasoft.replicator.model.replication.consumed;

import by.aurorasoft.replicator.testcrud.TestEntity;
import by.aurorasoft.replicator.testcrud.TestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.mockito.Mockito.*;

public final class SaveConsumedReplicationTest {

    @Test
    public void replicationShouldBeExecutedInternally() {
        TestEntity givenEntity = new TestEntity();
        SaveConsumedReplication<TestEntity, Long> givenReplication = new SaveConsumedReplication<>(givenEntity);
        JpaRepository<TestEntity, Long> givenRepository = mock(TestRepository.class);

        givenReplication.executeInternal(givenRepository);

        verify(givenRepository, times(1)).save(same(givenEntity));
    }
}
