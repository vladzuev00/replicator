package by.aurorasoft.replicator.config;

import by.aurorasoft.replicator.factory.ReplicatedServiceRegistryFactory;
import by.aurorasoft.replicator.factory.ReplicationObjectMapperFactory;
import by.aurorasoft.replicator.factory.ReplicationProducerRegistryFactory;
import by.aurorasoft.replicator.factory.ReplicationRetryTemplateFactory;
import by.aurorasoft.replicator.objectmapper.ReplicationObjectMapper;
import by.aurorasoft.replicator.registry.ReplicatedServiceRegistry;
import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.retry.support.RetryTemplate;

import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ReplicationConfigTest {
    private final ReplicationConfig config = new ReplicationConfig();

    @Test
    public void replicatedServiceRegistryShouldBeCreated() {
        ReplicatedServiceRegistryFactory givenFactory = mock(ReplicatedServiceRegistryFactory.class);

        ReplicatedServiceRegistry givenRegistry = mock(ReplicatedServiceRegistry.class);
        when(givenFactory.create()).thenReturn(givenRegistry);

        ReplicatedServiceRegistry actual = config.replicatedServiceRegistry(givenFactory);
        assertSame(givenRegistry, actual);
    }

    @Test
    public void replicationProducerRegistryShouldBeCreated() {
        ReplicationProducerRegistryFactory givenFactory = mock(ReplicationProducerRegistryFactory.class);

        ReplicationProducerRegistry givenRegistry = mock(ReplicationProducerRegistry.class);
        when(givenFactory.create()).thenReturn(givenRegistry);

        ReplicationProducerRegistry actual = config.replicationProducerRegistry(givenFactory);
        assertSame(givenRegistry, actual);
    }

    @Test
    public void replicationRetryTemplateShouldBeCreated() {
        ReplicationRetryTemplateFactory givenFactory = mock(ReplicationRetryTemplateFactory.class);

        RetryTemplate givenTemplate = mock(RetryTemplate.class);
        when(givenFactory.create()).thenReturn(givenTemplate);

        RetryTemplate actual = config.replicationRetryTemplate(givenFactory);
        assertSame(givenTemplate, actual);
    }

    @Test
    public void replicationObjectMapperShouldBeCreated() {
        ReplicationObjectMapperFactory givenFactory = mock(ReplicationObjectMapperFactory.class);
        ObjectMapper givenSource = mock(ObjectMapper.class);

        ReplicationObjectMapper givenMapper = mock(ReplicationObjectMapper.class);
        when(givenFactory.create(same(givenSource))).thenReturn(givenMapper);

        ReplicationObjectMapper actual = config.replicationObjectMapper(givenFactory, givenSource);
        assertSame(givenMapper, actual);
    }
}
