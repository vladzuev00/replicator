package by.aurorasoft.replicator.testutil;

import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
import lombok.experimental.UtilityClass;

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
}
