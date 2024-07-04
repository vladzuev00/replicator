package by.aurorasoft.replicator.registry.producer;

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
    public void producerShouldBeFoundForService() {
        final Object givenService = new Object();
        final ReplicationProducer givenProducer = mock(ReplicationProducer.class);
        final ReplicationProducerRegistry givenHolder = createHolder(givenService, givenProducer);

        final Optional<ReplicationProducer> optionalActual = givenHolder.get(givenService);
        assertTrue(optionalActual.isPresent());
        final ReplicationProducer actual = optionalActual.get();
        assertSame(givenProducer, actual);
    }

    @Test
    public void producerShouldNotBeFoundForService() {
        final Object firstGivenService = new Object();
        final Object secondGivenService = new Object();
        final ReplicationProducer givenProducer = mock(ReplicationProducer.class);
        final ReplicationProducerRegistry givenHolder = createHolder(firstGivenService, givenProducer);

        final Optional<ReplicationProducer> optionalActual = givenHolder.get(secondGivenService);
        assertTrue(optionalActual.isEmpty());
    }

    private ReplicationProducerRegistry createHolder(final Object service, final ReplicationProducer producer) {
        return new ReplicationProducerRegistry(Map.of(service, producer));
    }
}
