//package by.aurorasoft.replicator.factory;
//
//import by.aurorasoft.replicator.annotation.ReplicatedRepository.Producer;
//import org.apache.kafka.common.serialization.LongSerializer;
//import org.apache.kafka.common.serialization.Serializer;
//import org.junit.jupiter.api.Test;
//
//import static by.aurorasoft.replicator.testutil.ProducerConfigUtil.createProducerConfig;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//public final class ReplicationProducerKeySerializerFactoryTest {
//    private final ReplicationProducerKeySerializerFactory factory = new ReplicationProducerKeySerializerFactory();
//
//    @Test
//    public void serializerShouldBeCreated() {
//        Class<?> givenSerializerType = LongSerializer.class;
//        Producer givenConfig = createProducerConfig(givenSerializerType);
//
//        Serializer<Object> actual = factory.create(givenConfig);
//        assertNotNull(actual);
//        assertTrue(givenSerializerType.isInstance(actual));
//    }
//}
