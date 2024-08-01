package by.aurorasoft.replicator.testutil;

import by.aurorasoft.replicator.annotation.ReplicatedRepository.Producer;
import lombok.experimental.UtilityClass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UtilityClass
public final class ProducerConfigUtil {

    public static Producer createProducerConfig(int batchSize, int lingerMs, int deliveryTimeoutMs) {
        Producer config = mock(Producer.class);
        when(config.batchSize()).thenReturn(batchSize);
        when(config.lingerMs()).thenReturn(lingerMs);
        when(config.deliveryTimeoutMs()).thenReturn(deliveryTimeoutMs);
        return config;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Producer createProducerConfig(Class idSerializer,
                                                int batchSize,
                                                int lingerMs,
                                                int deliveryTimeoutMs) {
        Producer config = createProducerConfig(batchSize, lingerMs, deliveryTimeoutMs);
        when(config.idSerializer()).thenReturn(idSerializer);
        return config;
    }

    public static void checkEquals(Producer expected, Producer actual) {
        assertSame(expected.idSerializer(), actual.idSerializer());
        assertEquals(expected.batchSize(), actual.batchSize());
        assertEquals(expected.lingerMs(), actual.lingerMs());
        assertEquals(expected.deliveryTimeoutMs(), actual.deliveryTimeoutMs());
    }
}
