package by.aurorasoft.replicator.model.replication.consumed;

import by.aurorasoft.replicator.base.v2.entity.TestV2Entity;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.mockito.Mockito.*;

public final class SaveConsumedReplicationTest {

    @Test
    @SuppressWarnings("unchecked")
    public void replicationShouldBeExecutedInternally() {
        final TestV2Entity givenEntity = new TestV2Entity(255L);
        final SaveConsumedReplication<TestV2Entity, Long> givenReplication = new SaveConsumedReplication<>(givenEntity);

        final JpaRepository<TestV2Entity, Long> givenRepository = mock(JpaRepository.class);

        givenReplication.executeInternal(givenRepository);

        verify(givenRepository, times(1)).save(same(givenEntity));
    }
}
