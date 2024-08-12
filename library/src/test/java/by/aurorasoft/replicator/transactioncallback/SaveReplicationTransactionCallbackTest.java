package by.aurorasoft.replicator.transactioncallback;

import by.aurorasoft.replicator.producer.ReplicationProducer;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public final class SaveReplicationTransactionCallbackTest {
    private final SaveReplicationTransactionCallback callback = new SaveReplicationTransactionCallback(null, null);

    @Test
    public void saveShouldBeProduced() {
        Object givenSavedEntity = new Object();
        ReplicationProducer givenProducer = mock(ReplicationProducer.class);

        callback.produce(givenSavedEntity, givenProducer);

        verify(givenProducer, times(1)).produceSaveAfterCommit(same(givenSavedEntity));
    }
}
