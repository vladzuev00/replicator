//package by.aurorasoft.replicator.holder;
//
//import by.aurorasoft.replicator.producer.ReplicationProducer;
//import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.Map;
//import java.util.Optional;
//
//import static org.junit.Assert.assertSame;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Mockito.mock;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class ReplicationProducerHolderTest {
//
//    @Test
//    public void producerShouldBeFoundByService() {
//        final AbsServiceRUD<?, ?, ?, ?, ?> givenService = mock(AbsServiceRUD.class);
//        final ReplicationProducer<?, ?> givenProducer = mock(ReplicationProducer.class);
//        final Map<AbsServiceRUD<?, ?, ?, ?, ?>, ReplicationProducer<?, ?>> givenProducersByServices = Map.of(
//                givenService,
//                givenProducer
//        );
//        final ReplicationProducerHolder givenHolder = new ReplicationProducerHolder(givenProducersByServices);
//
//        final Optional<ReplicationProducer<?, ?>> optionalActual = givenHolder.findByService(givenService);
//        assertTrue(optionalActual.isPresent());
//        final ReplicationProducer<?, ?> actual = optionalActual.get();
//        assertSame(givenProducer, actual);
//    }
//
//    @Test
//    public void producerShouldNotBeFoundByService() {
//        final AbsServiceRUD<?, ?, ?, ?, ?> firstGivenService = mock(AbsServiceRUD.class);
//        final AbsServiceRUD<?, ?, ?, ?, ?> secondGivenService = mock(AbsServiceRUD.class);
//        final ReplicationProducer<?, ?> givenProducer = mock(ReplicationProducer.class);
//        final Map<AbsServiceRUD<?, ?, ?, ?, ?>, ReplicationProducer<?, ?>> givenProducersByServices = Map.of(
//                firstGivenService,
//                givenProducer
//        );
//        final ReplicationProducerHolder givenHolder = new ReplicationProducerHolder(givenProducersByServices);
//
//        final Optional<ReplicationProducer<?, ?>> optionalActual = givenHolder.findByService(secondGivenService);
//        assertTrue(optionalActual.isEmpty());
//    }
//}
