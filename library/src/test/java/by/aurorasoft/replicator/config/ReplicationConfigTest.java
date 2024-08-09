package by.aurorasoft.replicator.config;

import by.aurorasoft.replicator.factory.registry.ReplicationProducerRegistryFactory;
import by.aurorasoft.replicator.factory.retrytemplate.ReplicationRetryTemplateFactory;
import by.aurorasoft.replicator.model.setting.ReplicationProduceSetting;
import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.retry.support.RetryTemplate;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ReplicationConfigTest {
    private final ObjectMapper givenObjectMapper = new ObjectMapper();
    private final ReplicationConfig config = new ReplicationConfig(givenObjectMapper);

    @Test
    public void jsonViewModuleShouldBeRegistered() {
        Set<Object> actual = givenObjectMapper.getRegisteredModuleIds();
        Set<Object> expected = Set.of("json-view");
        assertEquals(expected, actual);
    }

    @Test
    public void replicationRetryTemplateShouldBeCreated() {
        ReplicationRetryTemplateFactory givenFactory = mock(ReplicationRetryTemplateFactory.class);

        RetryTemplate givenRetryTemplate = mock(RetryTemplate.class);
        when(givenFactory.create()).thenReturn(givenRetryTemplate);

        RetryTemplate actual = config.replicationRetryTemplate(givenFactory);
        assertSame(givenRetryTemplate, actual);
    }

    @Test
    public void replicationProducerRegistryShouldBeCreated() {
        ReplicationProducerRegistryFactory givenFactory = mock(ReplicationProducerRegistryFactory.class);
        @SuppressWarnings("unchecked") List<ReplicationProduceSetting<?, ?>> givenSettings = mock(List.class);

        ReplicationProducerRegistry givenRegistry = mock(ReplicationProducerRegistry.class);
        when(givenFactory.create(same(givenSettings))).thenReturn(givenRegistry);

        ReplicationProducerRegistry actual = config.replicationProducerRegistry(givenFactory, givenSettings);
        assertSame(givenRegistry, actual);
    }
}
