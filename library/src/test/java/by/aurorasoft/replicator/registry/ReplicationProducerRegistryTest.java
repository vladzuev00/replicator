package by.aurorasoft.replicator.registry;

import by.aurorasoft.replicator.producer.ReplicationProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public final class ReplicationProducerRegistryTest {

    @Mock
    private JpaRepository<?, ?> mockedRepository;

    @Mock
    private ReplicationProducer mockedProducer;

    private ReplicationProducerRegistry registry;

    @BeforeEach
    public void initializeRegistry() {
        registry = new ReplicationProducerRegistry(Map.of(mockedRepository, mockedProducer));
    }

    @Test
    public void producerShouldBeGot() {
        Optional<ReplicationProducer> optionalActual = registry.get(mockedRepository);
        assertTrue(optionalActual.isPresent());
        ReplicationProducer actual = optionalActual.get();
        assertSame(mockedProducer, actual);
    }

    @Test
    public void producerShouldNotBeGot() {
        JpaRepository<?, ?> givenRepository = mock(JpaRepository.class);

        Optional<ReplicationProducer> optionalActual = registry.get(givenRepository);
        assertTrue(optionalActual.isEmpty());
    }
}
