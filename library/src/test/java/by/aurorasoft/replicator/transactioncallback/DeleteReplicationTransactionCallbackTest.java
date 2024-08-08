package by.aurorasoft.replicator.transactioncallback;

import by.aurorasoft.replicator.producer.ReplicationProducer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public final class DeleteReplicationTransactionCallbackTest {
    private final DeleteReplicationTransactionCallback callback = new DeleteReplicationTransactionCallback(null, null);

    @Test
    public void deleteShouldBeProduced() {
        Object givenEntityId = new Object();
        ReplicationProducer givenProducer = Mockito.mock(ReplicationProducer.class);

        callback.produce(givenEntityId, givenProducer);

        verify(givenProducer, times(1)).produceDelete(same(givenEntityId));
    }
}
