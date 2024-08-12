//package by.aurorasoft.replicator.factory.retrytemplate;
//
//import by.aurorasoft.replicator.exception.RelatedReplicationNotDeliveredException;
//import by.aurorasoft.replicator.property.ReplicationRetryConsumeProperty;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockedConstruction;
//import org.springframework.retry.support.RetryTemplate;
//import org.springframework.retry.support.RetryTemplateBuilder;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertSame;
//import static org.mockito.Mockito.*;
//
//public final class ReplicationRetryTemplateFactoryTest {
//    private static final long GIVEN_TIME_LAPSE_MS = 255;
//    private static final int GIVEN_MAX_ATTEMPTS = 10;
//    private static final ReplicationRetryConsumeProperty GIVEN_PROPERTY = new ReplicationRetryConsumeProperty(
//            GIVEN_TIME_LAPSE_MS,
//            GIVEN_MAX_ATTEMPTS
//    );
//
//    private final ReplicationRetryTemplateFactory factory = new ReplicationRetryTemplateFactory(GIVEN_PROPERTY);
//
//    @Test
//    public void retryTemplateShouldBeCreated() {
//        RetryTemplate givenTemplate = mock(RetryTemplate.class);
//        try (
//                MockedConstruction<RetryTemplateBuilder> mockedBuilderConstruction = mockConstruction(
//                        RetryTemplateBuilder.class,
//                        (builder, context) -> configureBuilder(builder, givenTemplate)
//                )
//        ) {
//            factory.create();
//
//            List<RetryTemplateBuilder> constructedBuilders = mockedBuilderConstruction.constructed();
//            assertEquals(1, constructedBuilders.size());
//            RetryTemplateBuilder constructedBuilder = constructedBuilders.get(0);
//
//            RetryTemplate actual = constructedBuilder.build();
//            assertSame(givenTemplate, actual);
//
//            verify(constructedBuilder, times(1)).fixedBackoff(eq(GIVEN_TIME_LAPSE_MS));
//            verify(constructedBuilder, times(1)).maxAttempts(eq(GIVEN_MAX_ATTEMPTS));
//            verify(constructedBuilder, times(1)).retryOn(same(RelatedReplicationNotDeliveredException.class));
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    private void configureBuilder(RetryTemplateBuilder builder, RetryTemplate template) {
//        when(builder.fixedBackoff(anyLong())).thenReturn(builder);
//        when(builder.maxAttempts(anyInt())).thenReturn(builder);
//        when(builder.retryOn(any(Class.class))).thenReturn(builder);
//        when(builder.build()).thenReturn(template);
//    }
//}
