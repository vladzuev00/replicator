package by.aurorasoft.replicator.model.consumed;

import by.aurorasoft.replicator.base.entity.TestEntity;
import org.junit.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import static java.util.UUID.randomUUID;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public final class SaveConsumedReplicationTest {

    @Test
    @SuppressWarnings("unchecked")
    public void replicationShouldBeExecuted() {
//        final TestEntity givenEntity = new TestEntity(255L);
//        final SaveConsumedReplication<Long, TestEntity> givenReplication = new SaveConsumedReplication<>(
//                randomUUID(),
//                givenEntity
//        );
//
//        final JpaRepository<TestEntity, Long> givenRepository = mock(JpaRepository.class);
//
//        givenReplication.execute(givenRepository);
//
//        verify(givenRepository, times(1)).save(same(givenEntity));
        throw new RuntimeException();
    }
}
