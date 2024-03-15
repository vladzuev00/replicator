//package by.aurorasoft.replicator.factory;
//
//import by.aurorasoft.replicator.base.entity.TestEntity;
//import by.aurorasoft.replicator.consuming.ReplicationConsumingPipelineStarter;
//import com.fasterxml.jackson.core.type.TypeReference;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.junit.Test;
//import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
//import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
//import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;
//
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.Collection;
//
//import static by.aurorasoft.replicator.factory.ReplicationConsumerEndpointFactory.PROCESSING_METHOD_NAME;
//import static by.aurorasoft.replicator.util.ReflectionUtil.getFieldValue;
//import static java.util.Collections.singletonList;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertSame;
//
//public final class ReplicationConsumerEndpointFactoryTest {
//    private static final String FIELD_NAME_MESSAGE_HANDLER_METHOD_FACTORY = "messageHandlerMethodFactory";
//
//    private final ReplicationConsumerEndpointFactory factory = new ReplicationConsumerEndpointFactory();
//
//    @Test
//    public void endpointShouldBeCreated()
//            throws Exception {
//        final String givenGroupId = "sync-dto-group";
//        final String givenTopic = "sync-dto";
//        final ReplicationConsumingPipelineStarter<Long, TestEntity> givenConsumer = createConsumer(givenGroupId, givenTopic);
//
//        final MethodKafkaListenerEndpoint<?, ?> actual = (MethodKafkaListenerEndpoint<?, ?>) factory.create(givenConsumer);
//
//        final String actualGroupId = actual.getGroupId();
//        assertSame(givenGroupId, actualGroupId);
//
//        final Boolean actualAutoStartup = actual.getAutoStartup();
//        final Boolean expectedAutoStartup = true;
//        assertEquals(expectedAutoStartup, actualAutoStartup);
//
//        final Collection<String> actualTopics = new ArrayList<>(actual.getTopics());
//        final Collection<String> expectedTopics = singletonList(givenTopic);
//        assertEquals(expectedTopics, actualTopics);
//
//        final Class<?> actualMessageHandlerMethodFactory = getMessageHandlerMethodFactory(actual).getClass();
//        final Class<?> expectedMessageHandlerMethodFactory = DefaultMessageHandlerMethodFactory.class;
//        assertSame(expectedMessageHandlerMethodFactory, actualMessageHandlerMethodFactory);
//
//        final Object actualBean = actual.getBean();
//        assertSame(givenConsumer, actualBean);
//
//        final Method actualMethod = actual.getMethod();
//        final Method expectedMethod = getProcessingMethod();
//        assertEquals(expectedMethod, actualMethod);
//    }
//
//    @SuppressWarnings("SameParameterValue")
//    private ReplicationConsumingPipelineStarter<Long, TestEntity> createConsumer(final String groupId, final String topic) {
//        return new ReplicationConsumingPipelineStarter<>(
//                groupId,
//                topic,
//                null,
//                new TypeReference<>() {
//                },
//                null
//        );
//    }
//
//    private static MessageHandlerMethodFactory getMessageHandlerMethodFactory(
//            final MethodKafkaListenerEndpoint<?, ?> endpoint
//    ) {
//        return getFieldValue(endpoint, FIELD_NAME_MESSAGE_HANDLER_METHOD_FACTORY, MessageHandlerMethodFactory.class);
//    }
//
//    private static Method getProcessingMethod()
//            throws Exception {
//        return ReplicationConsumingPipelineStarter.class.getMethod(PROCESSING_METHOD_NAME, ConsumerRecord.class);
//    }
//}
