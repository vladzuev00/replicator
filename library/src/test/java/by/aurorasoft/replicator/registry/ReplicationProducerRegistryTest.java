package by.aurorasoft.replicator.registry;

import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.testcrud.TestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public final class ReplicationProducerRegistryTest {

    @Mock
    private TestService mockedTestService;

    @Mock
    private ReplicationProducer mockedProducer;

    private ReplicationProducerRegistry registry;

    @BeforeEach
    public void initializeRegistry() {
        registry = new ReplicationProducerRegistry(Map.of(mockedTestService, mockedProducer));
    }

    @Test
    public void producerShouldBeGot() {
        Optional<ReplicationProducer> optionalActual = registry.get(mockedTestService);
        assertTrue(optionalActual.isPresent());
        ReplicationProducer actual = optionalActual.get();
        assertSame(mockedProducer, actual);
    }

    @Test
    public void producerShouldNotBeGot() {
        Object givenService = new Object();

        Optional<ReplicationProducer> optionalActual = registry.get(givenService);
        assertTrue(optionalActual.isEmpty());
    }
}
