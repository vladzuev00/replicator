package by.aurorasoft.replicator.factory.kafkatemplate;

import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.model.setting.ReplicationProducerSetting;
import org.apache.kafka.common.serialization.Serializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class ReplicationKafkaTemplateFactoryTest {

    @Mock
    private ReplicationKafkaTemplateConfigsFactory mockedConfigsFactory;

    @Mock
    private ReplicationKafkaTemplateKeySerializerFactory mockedKeySerializerFactory;

    @Mock
    private ReplicationKafkaTemplateValueSerializerFactory mockedValueSerializerFactory;

    private ReplicationKafkaTemplateFactory kafkaTemplateFactory;

    @BeforeEach
    public void initializeKafkaTemplateFactory() {
        kafkaTemplateFactory = new ReplicationKafkaTemplateFactory(
                mockedConfigsFactory,
                mockedKeySerializerFactory,
                mockedValueSerializerFactory
        );
    }

    @Test
    public void kafkaTemplateShouldBeCreated() {
        ReplicationProducerSetting<?, ?> givenSetting = mock(ReplicationProducerSetting.class);

        Map<String, Object> givenConfigsByKeys = Map.of(
                "1", 1,
                "2", 2,
                "3", 3
        );
        when(mockedConfigsFactory.create(same(givenSetting))).thenReturn(givenConfigsByKeys);

        @SuppressWarnings("unchecked") Serializer<Object> givenKeySerializer = mock(Serializer.class);
        when(mockedKeySerializerFactory.create(same(givenSetting))).thenReturn(givenKeySerializer);

        @SuppressWarnings("unchecked") Serializer<ProducedReplication<?>> givenValueSerializer = mock(Serializer.class);
        when(mockedValueSerializerFactory.create()).thenReturn(givenValueSerializer);

        KafkaTemplate<Object, ProducedReplication<?>> actual = kafkaTemplateFactory.create(givenSetting);
        ProducerFactory<Object, ProducedReplication<?>> actualProducerFactory = actual.getProducerFactory();

        Map<String, Object> actualConfigsByKeys = actualProducerFactory.getConfigurationProperties();
        assertEquals(givenConfigsByKeys, actualConfigsByKeys);

        Serializer<Object> actualKeySerializer = actualProducerFactory.getKeySerializer();
        assertSame(givenKeySerializer, actualKeySerializer);

        Serializer<ProducedReplication<?>> actualValueSerializer = actualProducerFactory.getValueSerializer();
        assertSame(givenValueSerializer, actualValueSerializer);
    }
}
