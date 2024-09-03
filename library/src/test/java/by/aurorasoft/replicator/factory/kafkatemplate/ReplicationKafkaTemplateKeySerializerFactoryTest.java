//package by.aurorasoft.replicator.factory.kafkatemplate;
//
//import by.aurorasoft.replicator.testentity.TestEntity;
//import by.aurorasoft.replicator.testrepository.TestRepository;
//import org.apache.kafka.common.serialization.LongSerializer;
//import org.apache.kafka.common.serialization.Serializer;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public final class ReplicationKafkaTemplateKeySerializerFactoryTest {
//    private final ReplicationKafkaTemplateKeySerializerFactory factory = new ReplicationKafkaTemplateKeySerializerFactory();
//
//    @Test
//    public void serializerShouldBeCreated() {
//        Serializer<Long> givenIdSerializer = new LongSerializer();
//        ReplicationProduceSetting<TestEntity, Long> givenSetting = ReplicationProduceSetting.<TestEntity, Long>builder()
//                .topic("test-topic")
//                .repository(new TestRepository())
//                .idSerializer(givenIdSerializer)
//                .build();
//
//        Serializer<Object> actual = factory.create(givenSetting);
//        assertSame(givenIdSerializer, actual);
//    }
//}
