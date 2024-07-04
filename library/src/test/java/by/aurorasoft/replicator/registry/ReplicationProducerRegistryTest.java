package by.aurorasoft.replicator.registry;

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
public final class ReplicationProducerRegistryTest {

    @Test
    public void producerShouldBeGotService() {
        Object givenService = new Object();
        ReplicationProducer givenProducer = mock(ReplicationProducer.class);
        ReplicationProducerRegistry givenRegistry = createRegistry(givenService, givenProducer);

        Optional<ReplicationProducer> optionalActual = givenRegistry.get(givenService);
        assertTrue(optionalActual.isPresent());
        ReplicationProducer actual = optionalActual.get();
        assertSame(givenProducer, actual);
    }

    @Test
    public void producerShouldNotBeGotService() {
        Object firstGivenService = new Object();
        Object secondGivenService = new Object();
        ReplicationProducer givenProducer = mock(ReplicationProducer.class);
        ReplicationProducerRegistry givenRegistry = createRegistry(firstGivenService, givenProducer);

        Optional<ReplicationProducer> optionalActual = givenRegistry.get(secondGivenService);
        assertTrue(optionalActual.isEmpty());
    }

    private ReplicationProducerRegistry createRegistry(Object service, ReplicationProducer producer) {
        return new ReplicationProducerRegistry(Map.of(service, producer));
    }
}
