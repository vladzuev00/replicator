package by.aurorasoft.replicator.transactioncallback;

import by.aurorasoft.replicator.producer.ReplicationProducer;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public final class ReplicationTransactionCallbackTest {

    @Test
    public void bodyShouldBeProduced() {
        Object givenBody = new Object();
        ReplicationProducer givenProducer = mock(ReplicationProducer.class);
        ReplicationTransactionCallback givenCallback = new TestReplicationTransactionCallback(givenBody, givenProducer);

        givenCallback.afterCommit();

        verify(givenProducer, times(1)).produceSaveAfterCommit(same(givenBody));
    }

    private static final class TestReplicationTransactionCallback extends ReplicationTransactionCallback {

        public TestReplicationTransactionCallback(Object body, ReplicationProducer producer) {
            super(body, producer);
        }

        @Override
        protected void produce(Object body, ReplicationProducer producer) {
            producer.produceSaveAfterCommit(body);
        }
    }
}
