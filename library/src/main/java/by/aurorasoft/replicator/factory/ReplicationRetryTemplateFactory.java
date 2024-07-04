package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.exception.RelatedReplicationNotDeliveredException;
import by.aurorasoft.replicator.property.ReplicationRetryConsumeProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.support.RetryTemplateBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ReplicationRetryTemplateFactory {
    private final ReplicationRetryConsumeProperty property;

    public RetryTemplate create() {
        return new RetryTemplateBuilder()
                .fixedBackoff(property.getTimeLapseMs())
                .maxAttempts(property.getMaxAttempts())
                .retryOn(RelatedReplicationNotDeliveredException.class)
                .build();
    }

    //TODO for test
    /*
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
            final RetryTemplateBuilder constructedBuilder = constructedBuilders.get(0);

            final RetryTemplate actual = constructedBuilder.build();
            assertSame(givenTemplate, actual);

            verify(constructedBuilder, times(1)).fixedBackoff(eq(givenTimeLapseMs));
            verify(constructedBuilder, times(1)).maxAttempts(eq(givenMaxAttempts));
            verify(constructedBuilder, times(1)).retryOn(same(RelatedReplicationNotDeliveredException.class));
        }
    }

    @SuppressWarnings("unchecked")
    private void configureBuilder(final RetryTemplateBuilder builder, final RetryTemplate template) {
        when(builder.fixedBackoff(anyLong())).thenReturn(builder);
        when(builder.maxAttempts(anyInt())).thenReturn(builder);
        when(builder.retryOn(any(Class.class))).thenReturn(builder);
        when(builder.build()).thenReturn(template);
    }
     */
}
