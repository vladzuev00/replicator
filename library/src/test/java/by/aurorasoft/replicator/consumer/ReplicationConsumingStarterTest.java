package by.aurorasoft.replicator.consumer;

import by.aurorasoft.replicator.base.dto.TestDto;
import by.aurorasoft.replicator.base.service.FirstTestCRUDService;
import by.aurorasoft.replicator.base.service.SecondTestCRUDService;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static by.aurorasoft.replicator.util.ReflectionUtil.getFieldValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public final class ReplicationConsumingStarterTest {
    private static final String FIELD_NAME_CONSUMER_SERVICE = "service";
    private static final String FIELD_NAME_CONSUMER_OBJECT_MAPPER = "objectMapper";
    private static final String FIELD_NAME_CONSUMER_DTO_TYPE = "dtoType";

    @Mock
    private ObjectMapper mockedObjectMapper;

    @Mock
    private KafkaReplicationConsumerStarter mockedConsumerStarter;

    @Captor
    private ArgumentCaptor<KafkaReplicationConsumer<?, ?>> consumerArgumentCaptor;

    @Test
    public void consumersShouldBeStarted() {
        final String firstGivenGroupId = "first-group-id";
        final String firstGivenTopic = "first-topic";
        final Deserializer<Long> firstGivenIdDeserializer = new LongDeserializer();
        final Class<TestDto> firstGivenDtoType = TestDto.class;
        final AbsServiceCRUD<Long, ?, TestDto, ?> firstGivenService = new FirstTestCRUDService();

        final String secondGivenGroupId = "second-group-id";
        final String secondGivenTopic = "second-topic";
        final Deserializer<Long> secondGivenIdDeserializer = new LongDeserializer();
        final Class<TestDto> secondGivenDtoType = TestDto.class;
        final AbsServiceCRUD<Long, ?, TestDto, ?> secondGivenService = new SecondTestCRUDService();

        final ReplicationConsumingStarter givenStarter = createStarter(
                new KafkaReplicationConsumerConfig<>(
                        firstGivenGroupId,
                        firstGivenTopic,
                        firstGivenIdDeserializer,
                        firstGivenDtoType,
                        firstGivenService
                ),
                new KafkaReplicationConsumerConfig<>(
                        secondGivenGroupId,
                        secondGivenTopic,
                        secondGivenIdDeserializer,
                        secondGivenDtoType,
                        secondGivenService
                )
        );

        givenStarter.start();

        verify(mockedConsumerStarter, times(2)).start(consumerArgumentCaptor.capture());

        throw new RuntimeException();
    }

    private ReplicationConsumingStarter createStarter(final KafkaReplicationConsumerConfig<?, ?>... consumerConfigs) {
        return new ReplicationConsumingStarter(List.of(consumerConfigs), mockedObjectMapper, mockedConsumerStarter);
    }

    private static void checkEquals(final KafkaReplicationConsumer<?, ?> expected,
                                    final KafkaReplicationConsumer<?, ?> actual) {
        assertSame(getService(expected), getService(actual));
        assertSame(getObjectMapper(expected), getObjectMapper(actual));
        assertSame(getDtoType(expected), getDtoType(actual));
        assertEquals(expected.getGroupId(), actual.getGroupId());
        assertEquals(expected.getTopic(), actual.getTopic());
        assertEquals(expected.getIdDeserializer(), actual.getIdDeserializer());
    }

    private static AbsServiceCRUD<?, ?, ?, ?> getService(final KafkaReplicationConsumer<?, ?> consumer) {
        return getFieldValue(consumer, FIELD_NAME_CONSUMER_SERVICE, AbsServiceCRUD.class);
    }

    private static ObjectMapper getObjectMapper(final KafkaReplicationConsumer<?, ?> consumer) {
        return getFieldValue(consumer, FIELD_NAME_CONSUMER_OBJECT_MAPPER, ObjectMapper.class);
    }

    private static Class<?> getDtoType(final KafkaReplicationConsumer<?, ?> consumer) {
        return getFieldValue(consumer, FIELD_NAME_CONSUMER_DTO_TYPE, Class.class);
    }
}
