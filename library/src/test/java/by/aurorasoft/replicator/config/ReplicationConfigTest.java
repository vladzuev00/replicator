package by.aurorasoft.replicator.config;

import by.aurorasoft.replicator.factory.registry.DeleteReplicationProducerRegistryFactory;
import by.aurorasoft.replicator.factory.ReplicationRetryTemplateFactory;
import by.aurorasoft.replicator.factory.registry.SaveReplicationProducerRegistryFactory;
import by.aurorasoft.replicator.registry.replicationproducer.DeleteReplicationProducerRegistry;
import by.aurorasoft.replicator.registry.replicationproducer.SaveReplicationProducerRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.retry.support.RetryTemplate;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
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
    public void saveReplicationProducerRegistryShouldBeCreated() {
        SaveReplicationProducerRegistryFactory givenFactory = mock(SaveReplicationProducerRegistryFactory.class);

        SaveReplicationProducerRegistry givenRegistry = mock(SaveReplicationProducerRegistry.class);
        when(givenFactory.create()).thenReturn(givenRegistry);

        SaveReplicationProducerRegistry actual = config.saveReplicationProducerRegistry(givenFactory);
        assertSame(givenRegistry, actual);
    }

    @Test
    public void deleteReplicationProducerRegistryShouldBeCreated() {
        DeleteReplicationProducerRegistryFactory givenFactory = mock(DeleteReplicationProducerRegistryFactory.class);

        DeleteReplicationProducerRegistry givenRegistry = mock(DeleteReplicationProducerRegistry.class);
        when(givenFactory.create()).thenReturn(givenRegistry);

        DeleteReplicationProducerRegistry actual = config.deleteReplicationProducerRegistry(givenFactory);
        assertSame(givenRegistry, actual);
    }
}
