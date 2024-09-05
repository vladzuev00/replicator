package by.aurorasoft.replicator.model.replication.consumed;

import by.aurorasoft.replicator.testcrud.TestEntity;
import by.aurorasoft.replicator.testcrud.TestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.mockito.Mockito.*;

public final class DeleteConsumedReplicationTest {

    @Test
    public void replicationShouldBeExecutedInternally() {
        Long givenEntityId = 255L;
        DeleteConsumedReplication<TestEntity, Long> givenReplication = new DeleteConsumedReplication<>(givenEntityId);
        JpaRepository<TestEntity, Long> givenRepository = mock(TestRepository.class);

        givenReplication.executeInternal(givenRepository);

        verify(givenRepository, times(1)).deleteById(same(givenEntityId));
    }
}
