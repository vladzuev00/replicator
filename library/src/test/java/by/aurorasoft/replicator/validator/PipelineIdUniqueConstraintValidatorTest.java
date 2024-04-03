package by.aurorasoft.replicator.validator;

import by.aurorasoft.replicator.consuming.pipeline.ReplicationConsumePipeline;
import by.aurorasoft.replicator.event.PipelinesValidatedEvent;
import by.aurorasoft.replicator.validator.PipelineValidator.PipelineIdUniqueConstraintViolationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public final class PipelineIdUniqueConstraintValidatorTest {

    @Mock
    private ApplicationEventPublisher mockedEventPublisher;

    @Captor
    private ArgumentCaptor<ApplicationEvent> eventArgumentCaptor;

    @Test
    public void constraintShouldBeRespected() {
        final PipelineValidator givenValidator = createValidator(
                createPipeline("first-pipeline"),
                createPipeline("second-pipeline"),
                createPipeline("third-pipeline")
        );

        givenValidator.validate();

        verifySuccessValidation(givenValidator);
    }

    @Test
    public void constraintShouldNotBeRespected() {
        final PipelineValidator givenValidator = createValidator(
                createPipeline("first-pipeline"),
                createPipeline("second-pipeline"),
                createPipeline("second-pipeline"),
                createPipeline("third-pipeline"),
                createPipeline("third-pipeline"),
                createPipeline("third-pipeline"),
                createPipeline("fourth-pipeline")
        );

        validateExpectingFail(givenValidator, "Duplicated replication pipeline's ids were found: second-pipeline, third-pipeline");
    }

    @SuppressWarnings("unchecked")
    private static ReplicationConsumePipeline<?, ?> createPipeline(final String id) {
        return ReplicationConsumePipeline.builder()
                .id(id)
                .build();
    }

    private PipelineValidator createValidator(final ReplicationConsumePipeline<?, ?>... pipelines) {
        return new PipelineValidator(Arrays.asList(pipelines), mockedEventPublisher);
    }

    private void verifySuccessValidation(final PipelineValidator validator) {
        verify(mockedEventPublisher, times(1)).publishEvent(eventArgumentCaptor.capture());
        final ApplicationEvent capturedEvent = eventArgumentCaptor.getValue();
        assertTrue(capturedEvent instanceof PipelinesValidatedEvent);
        assertSame(validator, capturedEvent.getSource());
    }

    @SuppressWarnings("SameParameterValue")
    private void validateExpectingFail(final PipelineValidator validator, final String expectedMessage) {
        boolean exceptionArisen;
        try {
            validator.validate();
            exceptionArisen = false;
        } catch (final PipelineIdUniqueConstraintViolationException exception) {
            exceptionArisen = true;
            assertEquals(expectedMessage, exception.getMessage());
        }
        assertTrue(exceptionArisen);
    }
}
