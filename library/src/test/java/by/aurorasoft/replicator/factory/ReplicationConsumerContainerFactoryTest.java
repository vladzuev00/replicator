package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.base.entity.TestEntity;
import by.aurorasoft.replicator.consuming.consumer.ReplicationConsumer;
import by.aurorasoft.replicator.consuming.deserializer.ReplicationDeserializer;
import by.aurorasoft.replicator.model.consumed.ConsumedReplication;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.common.serialization.Deserializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;

import java.util.Map;

import static by.aurorasoft.replicator.util.ReflectionUtil.getFieldValue;
import static java.util.Collections.singletonList;
import static org.apache.kafka.clients.admin.AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class ReplicationConsumerContainerFactoryTest {
    private static final String FIELD_NAME_CONSUMER_FACTORY = "consumerFactory";

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
        final String givenGroupId = "test-dto-group";
        final Deserializer<Long> givenIdDeserializer = mock(Deserializer.class);
        final ReplicationConsumer<Long, TestEntity> givenConsumer = createConsumer(givenGroupId, givenIdDeserializer);

        final KafkaListenerEndpoint givenEndpoint = createEndPoint();
        when(mockedEndpointFactory.create(same(givenConsumer))).thenReturn(givenEndpoint);

        final ReplicationDeserializer<Long, TestEntity> givenReplicationDeserializer = mock(ReplicationDeserializer.class);
        when(mockedReplicationDeserializerFactory.create(same(givenConsumer))).thenReturn(givenReplicationDeserializer);

        final MessageListenerContainer actual = factory.create(givenConsumer);
        final ConsumerFactory<?, ?> actualConsumerFactory = getConsumerFactory(actual);

        final var actualConsumerFactoryConfigsByNames = actualConsumerFactory.getConfigurationProperties();
        final Map<String, Object> expectedConsumerFactoryConfigsByNames = Map.of(
                BOOTSTRAP_SERVERS_CONFIG, GIVEN_BOOTSTRAP_ADDRESS,
                GROUP_ID_CONFIG, givenGroupId
        );
        assertEquals(expectedConsumerFactoryConfigsByNames, actualConsumerFactoryConfigsByNames);

        final Deserializer<?> actualKeyDeserializer = actualConsumerFactory.getKeyDeserializer();
        assertSame(givenIdDeserializer, actualKeyDeserializer);

        final Deserializer<?> actualValueDeserializer = actualConsumerFactory.getValueDeserializer();
        assertSame(givenReplicationDeserializer, actualValueDeserializer);
    }

    @SuppressWarnings("SameParameterValue")
    private static ReplicationConsumer<Long, TestEntity> createConsumer(final String groupId,
                                                                        final Deserializer<Long> idDeserializer) {
        return new ReplicationConsumer<>(
                groupId,
                GIVEN_TOPIC,
                idDeserializer,
                new TypeReference<>() {
                },
                null
        );
    }

    private static KafkaListenerEndpoint createEndPoint() {
        final KafkaListenerEndpoint endpoint = mock(KafkaListenerEndpoint.class);
        when(endpoint.getTopics()).thenReturn(singletonList(GIVEN_TOPIC));
        when(endpoint.getConcurrency()).thenReturn(GIVEN_CONCURRENCY);
        return endpoint;
    }

    @SuppressWarnings("unchecked")
    private static ConsumerFactory<Long, ConsumedReplication<Long, TestEntity>> getConsumerFactory(
            final MessageListenerContainer container
    ) {
        return getFieldValue(container, FIELD_NAME_CONSUMER_FACTORY, ConsumerFactory.class);
    }
}
