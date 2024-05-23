package by.aurorasoft.replicator.holder.producer;

import by.aurorasoft.replicator.producer.ReplicationProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public final class ReplicationProducerHolderTest {

    @Test
    public void producerShouldBeFoundByService() {
        final Object givenService = new Object();
        final ReplicationProducer givenProducer = mock(ReplicationProducer.class);
        final ReplicationProducerHolder givenHolder = createHolder(givenService, givenProducer);

        final Optional<ReplicationProducer> optionalActual = givenHolder.findByService(givenService);
        assertTrue(optionalActual.isPresent());
        final ReplicationProducer actual = optionalActual.get();
        assertSame(givenProducer, actual);
    }

    @Test
    public void producerShouldNotBeFoundByService() {
        final Object firstGivenService = new Object();
        final Object secondGivenService = new Object();
        final ReplicationProducer givenProducer = mock(ReplicationProducer.class);
        final ReplicationProducerHolder givenHolder = createHolder(firstGivenService, givenProducer);

        final Optional<ReplicationProducer> optionalActual = givenHolder.findByService(secondGivenService);
        assertTrue(optionalActual.isEmpty());
    }

    private ReplicationProducerHolder createHolder(final Object service, final ReplicationProducer producer) {
        return new ReplicationProducerHolder(Map.of(service, producer));
    }
}
