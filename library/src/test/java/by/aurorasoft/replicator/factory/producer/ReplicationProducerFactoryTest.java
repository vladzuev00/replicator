package by.aurorasoft.replicator.factory.producer;

import by.aurorasoft.replicator.factory.kafkatemplate.ReplicationKafkaTemplateFactory;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.model.setting.ReplicationProduceSetting;
import by.aurorasoft.replicator.model.setting.ReplicationProduceSetting.EntityViewSetting;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class ReplicationProducerFactoryTest {

    @Mock
    private ReplicationKafkaTemplateFactory mockedKafkaTemplateFactory;

    private ReplicationProducerFactory producerFactory;

    @BeforeEach
    public void initializeProducerFactory() {
        producerFactory = new ReplicationProducerFactory(mockedKafkaTemplateFactory);
    }

    @Test
    public void producerShouldBeCreated() {
        String givenTopic = "test-topic";
        EntityViewSetting[] givenEntityViewSettings = {
                new EntityViewSetting(Object.class, new String[]{"firstProperty", "secondProperty"}),
                new EntityViewSetting(Integer.class, new String[]{"value"}),
        };
        ReplicationProduceSetting<?, ?> givenSetting = ReplicationProduceSetting.builder()
                .topic(givenTopic)
                .entityViewSettings(givenEntityViewSettings)
                .build();

        @SuppressWarnings("unchecked") KafkaTemplate<Object, ProducedReplication<?>> givenKafkaTemplate = mock(
                KafkaTemplate.class
        );
        when(mockedKafkaTemplateFactory.create(same(givenSetting))).thenReturn(givenKafkaTemplate);

        ReplicationProducer actual = producerFactory.create(givenSetting);

        KafkaTemplate<Object, ProducedReplication<?>> actualKafkaTemplate = actual.getKafkaTemplate();
        assertSame(givenKafkaTemplate, actualKafkaTemplate);

        String actualTopic = actual.getTopic();
        assertSame(givenTopic, actualTopic);

        EntityViewSetting[] actualEntityViewSettings = actual.getEntityViewSettings();
        assertSame(givenEntityViewSettings, actualEntityViewSettings);
    }
}
