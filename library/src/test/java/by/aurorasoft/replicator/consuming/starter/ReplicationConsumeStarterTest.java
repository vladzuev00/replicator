//package by.aurorasoft.replicator.consuming.starter;
//
//import by.aurorasoft.replicator.factory.kafkastreams.ReplicationKafkaStreamsFactory;
//import by.aurorasoft.replicator.model.setting.ReplicationConsumeSetting;
//import by.aurorasoft.replicator.validator.ReplicationUniqueComponentCheckingManager;
//import org.apache.kafka.streams.KafkaStreams;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public final class ReplicationConsumeStarterTest {
//
//    @Mock
//    private ReplicationUniqueComponentCheckingManager mockedUniqueComponentCheckingManager;
//
//    @Mock
//    private ReplicationKafkaStreamsFactory mockedKafkaStreamsFactory;
//
//    @Mock
//    private ReplicationConsumeSetting<?, ?> firstMockedSetting;
//
//    @Mock
//    private ReplicationConsumeSetting<?, ?> secondMockedSetting;
//
//    private ReplicationConsumeStarter starter;
//
//    @BeforeEach
//    public void initializeStarter() {
//        starter = new ReplicationConsumeStarter(
//                mockedUniqueComponentCheckingManager,
//                mockedKafkaStreamsFactory,
//                getGivenSettings()
//        );
//    }
//
//    @Test
//    public void consumingShouldBeStarted() {
//        KafkaStreams firstGivenKafkaStreams = mockKafkaStreamsFor(firstMockedSetting);
//        KafkaStreams secondGivenKafkaStreams = mockKafkaStreamsFor(secondMockedSetting);
//
//        starter.start();
//
//        verify(mockedUniqueComponentCheckingManager, times(1)).check(eq(getGivenSettings()));
//        verify(firstGivenKafkaStreams, times(1)).start();
//        verify(secondGivenKafkaStreams, times(1)).start();
//    }
//
//    private List<ReplicationConsumeSetting<?, ?>> getGivenSettings() {
//        return List.of(firstMockedSetting, secondMockedSetting);
//    }
//
//    private KafkaStreams mockKafkaStreamsFor(ReplicationConsumeSetting<?, ?> setting) {
//        KafkaStreams kafkaStreams = mock(KafkaStreams.class);
//        when(mockedKafkaStreamsFactory.create(same(setting))).thenReturn(kafkaStreams);
//        return kafkaStreams;
//    }
//}
