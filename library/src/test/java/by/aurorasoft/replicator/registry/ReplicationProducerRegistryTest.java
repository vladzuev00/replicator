//package by.aurorasoft.replicator.registry;
//
//import by.aurorasoft.replicator.producer.ReplicationProducer;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Map;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertSame;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.mock;
//
//@ExtendWith(MockitoExtension.class)
//public final class ReplicationProducerRegistryTest {
//    private static final Object GIVEN_SERVICE = new Object();
//    private static final ReplicationProducer GIVEN_PRODUCER = mock(ReplicationProducer.class);
//    private static final ReplicationProducerRegistry GIVEN_REGISTRY = new ReplicationProducerRegistry(
//            Map.of(GIVEN_SERVICE, GIVEN_PRODUCER)
//    );
//
//    @Test
//    public void producerShouldBeGotService() {
//        Optional<ReplicationProducer> optionalActual = GIVEN_REGISTRY.get(GIVEN_SERVICE);
//        assertTrue(optionalActual.isPresent());
//        ReplicationProducer actual = optionalActual.get();
//        assertSame(GIVEN_PRODUCER, actual);
//    }
//
//    @Test
//    public void producerShouldNotBeGotService() {
//        Object secondGivenService = new Object();
//
//        Optional<ReplicationProducer> optionalActual = GIVEN_REGISTRY.get(secondGivenService);
//        assertTrue(optionalActual.isEmpty());
//    }
//}
