package by.aurorasoft.replicator.testutil;

import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.Assertions;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UtilityClass
public final class ProducerConfigUtil {

    public static ProducerConfig createProducerConfig(int batchSize, int lingerMs, int deliveryTimeoutMs) {
        ProducerConfig config = mock(ProducerConfig.class);
        when(config.batchSize()).thenReturn(batchSize);
        when(config.lingerMs()).thenReturn(lingerMs);
        when(config.deliveryTimeoutMs()).thenReturn(deliveryTimeoutMs);
        return config;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static ProducerConfig createProducerConfig(Class idSerializerType) {
        ProducerConfig config = mock(ProducerConfig.class);
        when(config.idSerializer()).thenReturn(idSerializerType);
        return config;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static ProducerConfig createProducerConfig(Class idSerializerType, int batchSize, int lingerMs, int deliveryTimeoutMs) {
        ProducerConfig config = mock(ProducerConfig.class);
        when(config.idSerializer()).thenReturn(idSerializerType);
        when(config.batchSize()).thenReturn(batchSize);
        when(config.lingerMs()).thenReturn(lingerMs);
        when(config.deliveryTimeoutMs()).thenReturn(deliveryTimeoutMs);
        return config;
    }

    public static void assertEquals(ProducerConfig expected, ProducerConfig actual) {
        Assertions.assertSame(expected.idSerializer(), actual.idSerializer());
        Assertions.assertEquals(expected.batchSize(), actual.batchSize());
        Assertions.assertEquals(expected.lingerMs(), actual.lingerMs());
        Assertions.assertEquals(expected.deliveryTimeoutMs(), actual.deliveryTimeoutMs());
    }
}
