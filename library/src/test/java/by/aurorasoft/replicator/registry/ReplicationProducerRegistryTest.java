package by.aurorasoft.replicator.registry;

import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.testrepository.FirstTestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public final class ReplicationProducerRegistryTest {
    private static final JpaRepository<?, ?> GIVEN_REPOSITORY = new FirstTestRepository();
    private static final ReplicationProducer GIVEN_PRODUCER = mock(ReplicationProducer.class);
    private static final ReplicationProducerRegistry GIVEN_REGISTRY = new ReplicationProducerRegistry(
            Map.of(GIVEN_REPOSITORY, GIVEN_PRODUCER)
    );

    @Test
    public void producerShouldBeGotByRepository() {
        Optional<ReplicationProducer> optionalActual = GIVEN_REGISTRY.get(GIVEN_REPOSITORY);
        assertTrue(optionalActual.isPresent());
        ReplicationProducer actual = optionalActual.get();
        assertSame(GIVEN_PRODUCER, actual);
    }

    @Test
    public void producerShouldNotBeGotByRepository() {
        JpaRepository<?, ?> secondGivenRepository = mock(JpaRepository.class);

        Optional<ReplicationProducer> optionalActual = GIVEN_REGISTRY.get(secondGivenRepository);
        assertTrue(optionalActual.isEmpty());
    }
}
