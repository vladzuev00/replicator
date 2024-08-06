//package by.aurorasoft.replicator.model.replication.consumed;
//
//import by.aurorasoft.replicator.testentity.TestEntity;
//import org.junit.jupiter.api.Test;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import static org.mockito.Mockito.*;
//
//public final class DeleteConsumedReplicationTest {
//
//    @Test
//    public void replicationShouldBeExecutedInternally() {
//        Long givenEntityId = 255L;
//        var givenReplication = new DeleteConsumedReplication<TestEntity, Long>(givenEntityId);
//        @SuppressWarnings("unchecked") JpaRepository<TestEntity, Long> givenRepository = mock(JpaRepository.class);
//
//        givenReplication.executeInternal(givenRepository);
//
//        verify(givenRepository, times(1)).deleteById(same(givenEntityId));
//    }
//}
