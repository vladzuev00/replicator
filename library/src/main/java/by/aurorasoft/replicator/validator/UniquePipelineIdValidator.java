package by.aurorasoft.replicator.validator;

import by.aurorasoft.replicator.consuming.pipeline.ReplicationConsumePipeline;
import by.aurorasoft.replicator.event.UniquenessPipelineIdCheckedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import static java.lang.String.join;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

@Component
@RequiredArgsConstructor
public final class UniquePipelineIdValidator {
    private static final String VIOLATION_MESSAGE_TEMPLATE = "Duplicated replication pipeline's ids were found: %s";
    private static final String VIOLATION_MESSAGE_IDS_DELIMITER = ", ";

    private final List<ReplicationConsumePipeline<?, ?>> pipelines;
    private final ApplicationEventPublisher eventPublisher;

    @EventListener(ContextRefreshedEvent.class)
    public void validate() {
        final Set<String> duplicatedIds = findDuplicatedIds();
        if (duplicatedIds.isEmpty()) {
            publishSuccessEvent();
        } else {
            throwConstraintViolationException(duplicatedIds);
        }
    }

    private Set<String> findDuplicatedIds() {
        return pipelines.stream()
                .map(ReplicationConsumePipeline::getId)
                .collect(groupingBy(identity(), counting()))
                .entrySet()
                .stream()
                .filter(frequencyById -> frequencyById.getValue() > 1)
                .map(Entry::getKey)
                .collect(toUnmodifiableSet());
    }

    private void publishSuccessEvent() {
        eventPublisher.publishEvent(new UniquenessPipelineIdCheckedEvent(this));
    }

    private void throwConstraintViolationException(final Set<String> duplicatedIds) {
        final String message = createConstraintViolationMessage(duplicatedIds);
        throw new PipelineIdUniqueConstraintViolationException(message);
    }

    private String createConstraintViolationMessage(final Set<String> duplicatedIds) {
        return VIOLATION_MESSAGE_TEMPLATE.formatted(join(VIOLATION_MESSAGE_IDS_DELIMITER, duplicatedIds));
    }

    static final class PipelineIdUniqueConstraintViolationException extends RuntimeException {

        @SuppressWarnings("unused")
        public PipelineIdUniqueConstraintViolationException() {

        }

        public PipelineIdUniqueConstraintViolationException(final String description) {
            super(description);
        }

        @SuppressWarnings("unused")
        public PipelineIdUniqueConstraintViolationException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public PipelineIdUniqueConstraintViolationException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
