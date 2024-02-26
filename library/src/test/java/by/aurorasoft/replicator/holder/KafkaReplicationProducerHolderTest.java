package by.aurorasoft.replicator.holder;

import by.aurorasoft.replicator.producer.KafkaReplicationProducer;
import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public final class KafkaReplicationProducerHolderTest {

    @Test
    public void producerShouldBeFoundByService() {
        final AbsServiceRUD<?, ?, ?, ?, ?> givenService = mock(AbsServiceRUD.class);
        final KafkaReplicationProducer<?, ?> givenProducer = mock(KafkaReplicationProducer.class);
        final Map<AbsServiceRUD<?, ?, ?, ?, ?>, KafkaReplicationProducer<?, ?>> givenProducersByServices = Map.of(
                givenService,
                givenProducer
        );
        final KafkaReplicationProducerHolder givenHolder = new KafkaReplicationProducerHolder(givenProducersByServices);

        final Optional<KafkaReplicationProducer<?, ?>> optionalActual = givenHolder.findByService(givenService);
        assertTrue(optionalActual.isPresent());
        final KafkaReplicationProducer<?, ?> actual = optionalActual.get();
        assertSame(givenProducer, actual);
    }

    @Test
    public void producerShouldNotBeFoundByService() {
        final AbsServiceRUD<?, ?, ?, ?, ?> firstGivenService = mock(AbsServiceRUD.class);
        final AbsServiceRUD<?, ?, ?, ?, ?> secondGivenService = mock(AbsServiceRUD.class);
        final KafkaReplicationProducer<?, ?> givenProducer = mock(KafkaReplicationProducer.class);
        final Map<AbsServiceRUD<?, ?, ?, ?, ?>, KafkaReplicationProducer<?, ?>> givenProducersByServices = Map.of(
                firstGivenService,
                givenProducer
        );
        final KafkaReplicationProducerHolder givenHolder = new KafkaReplicationProducerHolder(givenProducersByServices);

        final Optional<KafkaReplicationProducer<?, ?>> optionalActual = givenHolder.findByService(secondGivenService);
        assertTrue(optionalActual.isEmpty());
    }
}
