package by.aurorasoft.replicator.model.replication.consumed;

import by.aurorasoft.replicator.v2.entity.TestV2Entity;
import org.junit.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.mockito.Mockito.*;

public final class DeleteConsumedReplicationTest {

    @Test
    @SuppressWarnings("unchecked")
    public void replicationShouldBeExecutedInternally() {
        final Long givenEntityId = 255L;
        final var givenReplication = new DeleteConsumedReplication<TestV2Entity, Long>(givenEntityId);

        final JpaRepository<TestV2Entity, Long> givenRepository = mock(JpaRepository.class);

        givenReplication.executeInternal(givenRepository);

        verify(givenRepository, times(1)).deleteById(same(givenEntityId));
    }
}
