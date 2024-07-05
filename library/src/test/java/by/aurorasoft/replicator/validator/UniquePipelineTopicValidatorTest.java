package by.aurorasoft.replicator.validator;

import by.aurorasoft.replicator.event.PipelinesValidatedEvent;
import by.aurorasoft.replicator.model.pipeline.ReplicationConsumePipeline;
import by.aurorasoft.replicator.validator.UniquePipelineTopicValidator.DuplicateReplicationTopicException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.common.serialization.Serde;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class UniquePipelineTopicValidatorTest {

    @Mock
    private ApplicationEventPublisher mockedEventPublisher;

    @Captor
    private ArgumentCaptor<ApplicationEvent> eventArgumentCaptor;

    @Test
    public void constraintShouldBeRespected() {
        UniquePipelineTopicValidator givenValidator = createValidator(
                createPipeline("first-topic"),
                createPipeline("second-topic"),
                createPipeline("third-topic")
        );

        givenValidator.validate();

        verifySuccessValidation(givenValidator);
    }

    @Test
    public void constraintShouldNotBeRespected() {
        UniquePipelineTopicValidator givenValidator = createValidator(
                createPipeline("first-topic"),
                createPipeline("second-topic"),
                createPipeline("second-topic"),
                createPipeline("third-topic"),
                createPipeline("third-topic"),
                createPipeline("third-topic"),
                createPipeline("fourth-topic")
        );

        validateExpectingFail(givenValidator, "Duplicated pipeline's topics were found: second-topic, third-topic");
    }

    @SuppressWarnings("unchecked")
    private static ReplicationConsumePipeline<?, ?> createPipeline(String topic) {
        return ReplicationConsumePipeline.builder()
                .topic(topic)
                .idSerde(mock(Serde.class))
                .replicationTypeReference(new TypeReference<>() {
                })
                .repository(mock(JpaRepository.class))
                .build();
    }

    private UniquePipelineTopicValidator createValidator(ReplicationConsumePipeline<?, ?>... pipelines) {
        return new UniquePipelineTopicValidator(Arrays.asList(pipelines), mockedEventPublisher);
    }

    private void verifySuccessValidation(UniquePipelineTopicValidator validator) {
        verify(mockedEventPublisher, times(1)).publishEvent(eventArgumentCaptor.capture());
        ApplicationEvent capturedEvent = eventArgumentCaptor.getValue();
        assertTrue(capturedEvent instanceof PipelinesValidatedEvent);
        assertSame(validator, capturedEvent.getSource());
    }

    @SuppressWarnings("SameParameterValue")
    private void validateExpectingFail(UniquePipelineTopicValidator validator, String expectedMessage) {
        boolean exceptionArisen;
        try {
            validator.validate();
            exceptionArisen = false;
        } catch (DuplicateReplicationTopicException exception) {
            exceptionArisen = true;
            assertEquals(expectedMessage, exception.getMessage());
        }
        assertTrue(exceptionArisen);
    }
}
