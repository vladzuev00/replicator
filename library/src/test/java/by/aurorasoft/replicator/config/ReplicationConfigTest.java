package by.aurorasoft.replicator.config;

import by.aurorasoft.replicator.exception.RelatedReplicationNotDeliveredException;
import by.aurorasoft.replicator.factory.ReplicationProducerHolderFactory;
import by.aurorasoft.replicator.holder.ReplicationProducerHolder;
import by.aurorasoft.replicator.property.ReplicationRetryConsumeProperty;
import org.junit.Test;
import org.mockito.MockedConstruction;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.support.RetryTemplateBuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public final class ReplicationConfigTest {
    private final ReplicationConfig config = new ReplicationConfig();

    @Test
    public void producerHolderShouldBeCreated() {
        final ReplicationProducerHolderFactory givenFactory = mock(ReplicationProducerHolderFactory.class);

        final ReplicationProducerHolder givenHolder = mock(ReplicationProducerHolder.class);
        when(givenFactory.create()).thenReturn(givenHolder);

        final ReplicationProducerHolder actual = config.replicationProducerHolder(givenFactory);
        assertSame(givenHolder, actual);
    }

    @Test
    public void retryTemplateShouldBeCreated() {
        final ReplicationRetryConsumeProperty givenProperty = new ReplicationRetryConsumeProperty(255, 10);
        final RetryTemplate givenTemplate = mock(RetryTemplate.class);

        try (final MockedConstruction<RetryTemplateBuilder> mockedBuilderConstruction = mockConstruction(RetryTemplateBuilder.class, (builder, context) -> configureBuilder(builder, givenTemplate))) {
            config.replicationRetryTemplate(givenProperty);

            verifyConstruction(mockedBuilderConstruction);
            final RetryTemplateBuilder builder = getConstructedObject(mockedBuilderConstruction);

            final RetryTemplate actual = builder.build();
            assertSame(givenTemplate, actual);

            verify(builder, times(1)).fixedBackoff(eq(givenProperty.getTimeLapseMs()));
            verify(builder, times(1)).maxAttempts(eq(givenProperty.getMaxAttempts()));
            verify(builder, times(1)).retryOn(same(RelatedReplicationNotDeliveredException.class));
        }
    }

    @SuppressWarnings("unchecked")
    private static void configureBuilder(final RetryTemplateBuilder builder, final RetryTemplate template) {
        when(builder.fixedBackoff(anyLong())).thenReturn(builder);
        when(builder.maxAttempts(anyInt())).thenReturn(builder);
        when(builder.retryOn(any(Class.class))).thenReturn(builder);
        when(builder.build()).thenReturn(template);
    }

    private void verifyConstruction(final MockedConstruction<?> construction) {
        assertEquals(1, construction.constructed().size());
    }

    private <T> T getConstructedObject(final MockedConstruction<T> mockedConstruction) {
        return mockedConstruction.constructed().get(0);
    }
}
