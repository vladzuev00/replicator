package by.aurorasoft.replicator.config;

import by.aurorasoft.replicator.factory.ReplicatedRepositoryRegistryFactory;
import by.aurorasoft.replicator.factory.ReplicationObjectMapperWrapperFactory;
import by.aurorasoft.replicator.factory.ReplicationProducerRegistryFactory;
import by.aurorasoft.replicator.factory.ReplicationRetryTemplateFactory;
import by.aurorasoft.replicator.objectmapper.ReplicationObjectMapperWrapper;
import by.aurorasoft.replicator.registry.ReplicatedRepositoryRegistry;
import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.retry.support.RetryTemplate;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ReplicationConfigTest {
    private final ReplicationConfig config = new ReplicationConfig();

    @Test
    public void replicatedRepositoryRegistryShouldBeCreated() {
        ReplicatedRepositoryRegistryFactory givenFactory = mock(ReplicatedRepositoryRegistryFactory.class);

        ReplicatedRepositoryRegistry givenRegistry = mock(ReplicatedRepositoryRegistry.class);
        when(givenFactory.create()).thenReturn(givenRegistry);

        ReplicatedRepositoryRegistry actual = config.replicatedRepositoryRegistry(givenFactory);
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
    public void replicationObjectMapperWrapperShouldBeCreated() {
        ReplicationObjectMapperWrapperFactory givenFactory = mock(ReplicationObjectMapperWrapperFactory.class);
        ObjectMapper givenSource = mock(ObjectMapper.class);

        ReplicationObjectMapperWrapper givenWrapper = mock(ReplicationObjectMapperWrapper.class);
        when(givenFactory.create(same(givenSource))).thenReturn(givenWrapper);

        ReplicationObjectMapperWrapper actual = config.replicationObjectMapperWrapper(givenFactory, givenSource);
        assertSame(givenWrapper, actual);
    }
}
