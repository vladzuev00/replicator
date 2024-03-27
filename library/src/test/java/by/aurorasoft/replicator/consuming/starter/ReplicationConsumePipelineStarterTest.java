//package by.aurorasoft.replicator.consuming.starter;
//
//import by.aurorasoft.replicator.base.entity.TestEntity;
//import by.aurorasoft.replicator.consuming.pipeline.ReplicationConsumePipeline;
//import by.aurorasoft.replicator.consuming.serde.ReplicationSerde;
//import by.aurorasoft.replicator.model.consumed.ConsumedReplication;
//import com.fasterxml.jackson.core.type.TypeReference;
//import org.apache.kafka.common.serialization.Serde;
//import org.apache.kafka.common.serialization.Serdes.LongSerde;
//import org.apache.kafka.streams.StreamsBuilder;
//import org.apache.kafka.streams.kstream.Consumed;
//import org.apache.kafka.streams.kstream.ForeachAction;
//import org.apache.kafka.streams.kstream.KStream;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import static by.aurorasoft.replicator.util.ReflectionUtil.getFieldValue;
//import static org.apache.kafka.common.serialization.Serdes.Long;
//import static org.junit.Assert.assertSame;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.same;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class ReplicationConsumePipelineStarterTest {
//    private static final String GIVEN_TOPIC = "sync-dto";
//
//    private static final String FIELD_NAME_KEY_SERDE = "keySerde";
//    private static final String FIELD_NAME_VALUE_SERDE = "valueSerde";
//
//    private final ReplicationConsumePipelineStarter starter = new ReplicationConsumePipelineStarter();
//
//    @Mock
//    private StreamsBuilder mockedStreamsBuilder;
//
//    @Mock
//    private JpaRepository<TestEntity, Long> mockedRepository;
//
//    @Captor
//    private ArgumentCaptor<Consumed<Long, ConsumedReplication<Long, TestEntity>>> consumedArgumentCaptor;
//
//    @Captor
//    private ArgumentCaptor<ForeachAction<Long, ConsumedReplication<Long, TestEntity>>> foreachActionArgumentCaptor;
//
//    @Test
//    @SuppressWarnings("unchecked")
//    public void pipelineShouldBeStarted() {
//        final ReplicationConsumePipeline<Long, TestEntity> givenPipeline = createPipeline();
//
//        final KStream<Long, ConsumedReplication<Long, TestEntity>> givenStream = mock(KStream.class);
//        when(mockedStreamsBuilder.stream(same(GIVEN_TOPIC), any(Consumed.class))).thenReturn(givenStream);
//
//        starter.start(givenPipeline, mockedStreamsBuilder);
//
//        verifyConsumed(LongSerde.class, ReplicationSerde.class);
//        verifyForeachAction(givenStream);
//    }
//
//    private ReplicationConsumePipeline<Long, TestEntity> createPipeline() {
//        return new ReplicationConsumePipeline<>(
//                GIVEN_TOPIC,
//                Long(),
//                new TypeReference<>() {
//                },
//                mockedRepository
//        );
//    }
//
//    @SuppressWarnings("SameParameterValue")
//    private void verifyConsumed(final Class<?> idSerdeType, final Class<?> valueSerdeType) {
//        verify(mockedStreamsBuilder, times(1))
//                .stream(same(GIVEN_TOPIC), consumedArgumentCaptor.capture());
//        final Consumed<Long, ConsumedReplication<Long, TestEntity>> actual = consumedArgumentCaptor.getValue();
//        assertSame(idSerdeType, getKeySerdeType(actual));
//        assertSame(valueSerdeType, getValueSerdeType(actual));
//    }
//
//    private static Class<?> getKeySerdeType(final Consumed<?, ?> consumed) {
//        return getSerdeType(consumed, FIELD_NAME_KEY_SERDE);
//    }
//
//    private static Class<?> getValueSerdeType(final Consumed<?, ?> consumed) {
//        return getSerdeType(consumed, FIELD_NAME_VALUE_SERDE);
//    }
//
//    private static Class<?> getSerdeType(final Consumed<?, ?> consumed, final String fieldName) {
//        return getFieldValue(consumed, fieldName, Serde.class).getClass();
//    }
//
//    @SuppressWarnings("unchecked")
//    private void verifyForeachAction(final KStream<Long, ConsumedReplication<Long, TestEntity>> stream) {
//        verify(stream, times(1)).foreach(foreachActionArgumentCaptor.capture());
//        final ForeachAction<Long, ConsumedReplication<Long, TestEntity>> actual = foreachActionArgumentCaptor.getValue();
//        final Long givenId = 255L;
//        final ConsumedReplication<Long, TestEntity> givenReplication = mock(ConsumedReplication.class);
//        actual.apply(givenId, givenReplication);
//        verify(givenReplication, times(1)).executeInternal(same(mockedRepository));
//    }
//}
