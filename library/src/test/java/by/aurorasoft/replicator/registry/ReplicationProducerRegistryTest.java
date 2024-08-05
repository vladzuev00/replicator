package by.aurorasoft.replicator.registry;

import by.aurorasoft.replicator.producer.KafkaReplicationProducer;
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
    private static final KafkaReplicationProducer GIVEN_PRODUCER = mock(KafkaReplicationProducer.class);
    private static final ReplicationProducerRegistry GIVEN_REGISTRY = new ReplicationProducerRegistry(
            Map.of(GIVEN_REPOSITORY, GIVEN_PRODUCER)
    );

    @Test
    public void producerShouldBeGotByRepository() {
        Optional<KafkaReplicationProducer> optionalActual = GIVEN_REGISTRY.get(GIVEN_REPOSITORY);
        assertTrue(optionalActual.isPresent());
        KafkaReplicationProducer actual = optionalActual.get();
        assertSame(GIVEN_PRODUCER, actual);
    }

    @Test
    public void producerShouldNotBeGotByRepository() {
        JpaRepository<?, ?> secondGivenRepository = mock(JpaRepository.class);

        Optional<KafkaReplicationProducer> optionalActual = GIVEN_REGISTRY.get(secondGivenRepository);
        assertTrue(optionalActual.isEmpty());
    }
}
