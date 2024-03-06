package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.base.dto.TestDto;
import by.aurorasoft.replicator.consuming.consumer.ReplicationConsumer;
import by.aurorasoft.replicator.consuming.consumer.ReplicationConsumerConfig;
import by.aurorasoft.replicator.consuming.deserializer.ReplicationDeserializer;
import org.apache.kafka.common.serialization.Deserializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.listener.MessageListenerContainer;

import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class ReplicationConsumerContainerFactoryTest {
    private static final String GIVEN_BOOTSTRAP_ADDRESS = "127.0.0.1:9092";
    private static final String GIVEN_TOPIC = "sync-dto";
    private static final int GIVEN_CONCURRENCY = 1;

    @Mock
    private ReplicationConsumerEndpointFactory mockedEndpointFactory;

    @Mock
    private ReplicationDeserializerFactory mockedReplicationDeserializerFactory;

    private ReplicationConsumerContainerFactory factory;

    @Before
    public void initializeFactory() {
        factory = new ReplicationConsumerContainerFactory(
                mockedEndpointFactory,
                mockedReplicationDeserializerFactory,
                GIVEN_BOOTSTRAP_ADDRESS
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    public void containerShouldBeCreated() {
        final ReplicationConsumer<Long, TestDto> givenConsumer = mock(ReplicationConsumer.class);

        final Deserializer<Long> givenDeserializer = mock(Deserializer.class);
        final String givenGroupId = "test-dto-group";
        final ReplicationConsumerConfig<Long, TestDto> givenConfig = createConsumerConfig(
                givenDeserializer,
                givenGroupId
        );
        when(givenConsumer.getConfig()).thenReturn(givenConfig);

        final KafkaListenerEndpoint givenEndpoint = createEndPoint();
        when(mockedEndpointFactory.create(same(givenConsumer))).thenReturn(givenEndpoint);

        final ReplicationDeserializer<Long, TestDto> givenReplicationDeserializer = mock(ReplicationDeserializer.class);
        when(mockedReplicationDeserializerFactory.create(givenConfig)).thenReturn(givenReplicationDeserializer);

        final MessageListenerContainer actual = factory.create(givenConsumer);

        throw new RuntimeException();
    }

    @SuppressWarnings("SameParameterValue")
    private ReplicationConsumerConfig<Long, TestDto> createConsumerConfig(final Deserializer<Long> idDeserializer,
                                                                          final String groupId) {
        return ReplicationConsumerConfig.<Long, TestDto>builder()
                .idDeserializer(idDeserializer)
                .groupId(groupId)
                .build();
    }

    private static KafkaListenerEndpoint createEndPoint() {
        final KafkaListenerEndpoint endpoint = mock(KafkaListenerEndpoint.class);
        when(endpoint.getTopics()).thenReturn(singletonList(GIVEN_TOPIC));
        when(endpoint.getConcurrency()).thenReturn(GIVEN_CONCURRENCY);
        return endpoint;
    }
}
