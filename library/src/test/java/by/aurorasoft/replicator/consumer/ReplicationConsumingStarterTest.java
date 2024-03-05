//package by.aurorasoft.replicator.consumer;
//
//import by.aurorasoft.replicator.base.dto.TestDto;
//import by.aurorasoft.replicator.base.service.FirstTestCRUDService;
//import by.aurorasoft.replicator.base.service.SecondTestCRUDService;
//import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
//import org.apache.kafka.common.serialization.Deserializer;
//import org.apache.kafka.common.serialization.LongDeserializer;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.List;
//
//import static by.aurorasoft.replicator.util.ReflectionUtil.getFieldValue;
//import static java.util.stream.IntStream.range;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertSame;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class ReplicationConsumingStarterTest {
//    private static final String FIELD_NAME_CONSUMER_SERVICE = "service";
//
//    @Mock
//    private KafkaReplicationConsumerStarter mockedConsumerStarter;
//
//    @Captor
//    private ArgumentCaptor<KafkaReplicationConsumer<?, ?>> consumerArgumentCaptor;
//
//    @Test
//    public void consumersShouldBeStarted() {
//        final String firstGivenGroupId = "first-group-id";
//        final String firstGivenTopic = "first-topic";
//        final Deserializer<Long> firstGivenIdDeserializer = new LongDeserializer();
//        final AbsServiceCRUD<Long, ?, TestDto, ?> firstGivenService = new FirstTestCRUDService();
//
//        final String secondGivenGroupId = "second-group-id";
//        final String secondGivenTopic = "second-topic";
//        final Deserializer<Long> secondGivenIdDeserializer = new LongDeserializer();
//        final AbsServiceCRUD<Long, ?, TestDto, ?> secondGivenService = new SecondTestCRUDService();
//
//        final ReplicationConsumingStarter givenStarter = createStarter(
//                new KafkaReplicationConsumerConfig<>(
//                        firstGivenGroupId,
//                        firstGivenTopic,
//                        firstGivenIdDeserializer,
//                        firstGivenService
//                ),
//                new KafkaReplicationConsumerConfig<>(
//                        secondGivenGroupId,
//                        secondGivenTopic,
//                        secondGivenIdDeserializer,
//                        secondGivenService
//                )
//        );
//
//        givenStarter.start();
//
//        verify(mockedConsumerStarter, times(2)).start(consumerArgumentCaptor.capture());
//
//        final List<KafkaReplicationConsumer<?, ?>> actualStartedConsumers = consumerArgumentCaptor.getAllValues();
//        final List<KafkaReplicationConsumer<?, ?>> expectedStartedConsumers = List.of(
//                new KafkaReplicationConsumer<>(
//                        firstGivenService,
//                        firstGivenGroupId,
//                        firstGivenTopic,
//                        firstGivenIdDeserializer
//                ),
//                new KafkaReplicationConsumer<>(
//                        secondGivenService,
//                        secondGivenGroupId,
//                        secondGivenTopic,
//                        secondGivenIdDeserializer
//                )
//        );
//        checkEquals(expectedStartedConsumers, actualStartedConsumers);
//    }
//
//    private ReplicationConsumingStarter createStarter(final KafkaReplicationConsumerConfig<?, ?>... consumerConfigs) {
//        return new ReplicationConsumingStarter(List.of(consumerConfigs), mockedConsumerStarter);
//    }
//
//    private static void checkEquals(final List<KafkaReplicationConsumer<?, ?>> expected,
//                                    final List<KafkaReplicationConsumer<?, ?>> actual) {
//        assertEquals(expected.size(), actual.size());
//        range(0, expected.size()).forEach(i -> checkEquals(expected.get(i), actual.get(i)));
//    }
//
//    private static void checkEquals(final KafkaReplicationConsumer<?, ?> expected,
//                                    final KafkaReplicationConsumer<?, ?> actual) {
//        assertSame(getService(expected), getService(actual));
//        assertEquals(expected.getGroupId(), actual.getGroupId());
//        assertEquals(expected.getTopic(), actual.getTopic());
//        assertEquals(expected.getIdDeserializer(), actual.getIdDeserializer());
//    }
//
//    private static AbsServiceCRUD<?, ?, ?, ?> getService(final KafkaReplicationConsumer<?, ?> consumer) {
//        return getFieldValue(consumer, FIELD_NAME_CONSUMER_SERVICE, AbsServiceCRUD.class);
//    }
//}
