package by.aurorasoft.replicator.config;

import by.aurorasoft.replicator.exception.RelatedReplicationNotDeliveredException;
import by.aurorasoft.replicator.factory.ReplicationProducerHolderFactory;
import by.aurorasoft.replicator.holder.ReplicationProducerHolder;
import by.aurorasoft.replicator.property.ReplicationRetryConsumeProperty;
import org.junit.Test;
import org.mockito.MockedConstruction;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.support.RetryTemplateBuilder;

import java.util.List;

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
        final long givenTimeLapseMs = 255;
        final int givenMaxAttempts = 10;
        final var givenProperty = new ReplicationRetryConsumeProperty(givenTimeLapseMs, givenMaxAttempts);
        final RetryTemplate givenTemplate = mock(RetryTemplate.class);

        try (
                final MockedConstruction<RetryTemplateBuilder> mockedBuilderConstruction = mockConstruction(
                        RetryTemplateBuilder.class,
                        (builder, context) -> configureBuilder(builder, givenTemplate)
                )
        ) {
            config.replicationRetryTemplate(givenProperty);

            final List<RetryTemplateBuilder> constructedBuilders = mockedBuilderConstruction.constructed();
            assertEquals(1, constructedBuilders.size());
            final RetryTemplateBuilder builder = constructedBuilders.get(0);

            final RetryTemplate actual = builder.build();
            assertSame(givenTemplate, actual);

            verify(builder, times(1)).fixedBackoff(eq(givenTimeLapseMs));
            verify(builder, times(1)).maxAttempts(eq(givenMaxAttempts));
            verify(builder, times(1)).retryOn(same(RelatedReplicationNotDeliveredException.class));
        }
    }

    @SuppressWarnings("unchecked")
    private void configureBuilder(final RetryTemplateBuilder builder, final RetryTemplate template) {
        when(builder.fixedBackoff(anyLong())).thenReturn(builder);
        when(builder.maxAttempts(anyInt())).thenReturn(builder);
        when(builder.retryOn(any(Class.class))).thenReturn(builder);
        when(builder.build()).thenReturn(template);
    }
}
