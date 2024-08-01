//package by.aurorasoft.replicator.factory;
//
//import by.aurorasoft.replicator.annotation.ReplicatedRepository;
//import by.aurorasoft.replicator.annotation.ReplicatedRepository.Producer;
//import by.aurorasoft.replicator.annotation.ReplicatedRepository.Topic;
//import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
//import by.aurorasoft.replicator.producer.ReplicationProducer;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.kafka.core.KafkaTemplate;
//
//import static by.aurorasoft.replicator.testutil.ReflectionUtil.getFieldValue;
//import static by.aurorasoft.replicator.testutil.TopicConfigUtil.createTopicConfig;
//import static org.junit.jupiter.api.Assertions.assertSame;
//import static org.mockito.ArgumentMatchers.same;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public final class ReplicationProducerFactoryTest {
//    private static final String FIELD_NAME_TOPIC_NAME = "topicName";
//    private static final String FIELD_NAME_KAFKA_TEMPLATE = "kafkaTemplate";
//
//    @Mock
//    private ReplicationKafkaTemplateFactory mockedKafkaTemplateFactory;
//
//    private ReplicationProducerFactory producerFactory;
//
//    @BeforeEach
//    public void initializeProducerFactory() {
//        producerFactory = new ReplicationProducerFactory(mockedKafkaTemplateFactory);
//    }
//
//    @Test
//    public void producerShouldBeCreated() {
//        String givenTopicName = "sync-dto";
//        Producer givenProducerConfig = mock(Producer.class);
//        ReplicatedRepository givenReplicatedService = createReplicatedService(givenTopicName, givenProducerConfig);
//        KafkaTemplate<Object, ProducedReplication<?>> givenKafkaTemplate = mockKafkaTemplateFor(givenProducerConfig);
//
//        ReplicationProducer actual = producerFactory.create(givenReplicatedService);
//
//        String actualTopicName = getTopicName(actual);
//        assertSame(givenTopicName, actualTopicName);
//
//        KafkaTemplate<?, ?> actualKafkaTemplate = getKafkaTemplate(actual);
//        assertSame(givenKafkaTemplate, actualKafkaTemplate);
//    }
//
//    private ReplicatedRepository createReplicatedService(String topicName, Producer producerConfig) {
//        Topic topicConfig = createTopicConfig(topicName);
//        ReplicatedRepository service = mock(ReplicatedRepository.class);
//        when(service.topic()).thenReturn(topicConfig);
//        when(service.producer()).thenReturn(producerConfig);
//        return service;
//    }
//
//
//    @SuppressWarnings("unchecked")
//    private KafkaTemplate<Object, ProducedReplication<?>> mockKafkaTemplateFor(Producer producerConfig) {
//        KafkaTemplate<Object, ProducedReplication<?>> kafkaTemplate = mock(KafkaTemplate.class);
//        when(mockedKafkaTemplateFactory.create(same(producerConfig))).thenReturn(kafkaTemplate);
//        return kafkaTemplate;
//    }
//
//    private String getTopicName(ReplicationProducer producer) {
//        return getFieldValue(producer, FIELD_NAME_TOPIC_NAME, String.class);
//    }
//
//    private KafkaTemplate<?, ?> getKafkaTemplate(ReplicationProducer producer) {
//        return getFieldValue(producer, FIELD_NAME_KAFKA_TEMPLATE, KafkaTemplate.class);
//    }
//}
