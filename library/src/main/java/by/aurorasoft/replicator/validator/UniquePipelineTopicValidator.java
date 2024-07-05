package by.aurorasoft.replicator.validator;

import by.aurorasoft.replicator.event.PipelinesValidatedEvent;
import by.aurorasoft.replicator.model.pipeline.ReplicationConsumePipeline;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import static java.lang.String.join;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

@Component
@RequiredArgsConstructor
public final class UniquePipelineTopicValidator {
    private static final String VIOLATION_MESSAGE_TEMPLATE = "Duplicated pipeline's topics were found: %s";
    private static final String VIOLATION_MESSAGE_TOPICS_DELIMITER = ", ";

    private final List<ReplicationConsumePipeline<?, ?>> pipelines;
    private final ApplicationEventPublisher eventPublisher;

    @EventListener(ContextRefreshedEvent.class)
    public void validate() {
        Set<String> duplicatedTopics = findDuplicatedTopics();
        if (duplicatedTopics.isEmpty()) {
            publishSuccessEvent();
        } else {
            throwConstraintViolationException(duplicatedTopics);
        }
    }

    private Set<String> findDuplicatedTopics() {
        return pipelines.stream()
                .map(ReplicationConsumePipeline::getTopic)
                .collect(groupingBy(identity(), LinkedHashMap::new, counting()))
                .entrySet()
                .stream()
                .filter(frequencyByTopic -> frequencyByTopic.getValue() > 1)
                .map(Entry::getKey)
                .collect(toCollection(LinkedHashSet::new));
    }

    private void publishSuccessEvent() {
        eventPublisher.publishEvent(new PipelinesValidatedEvent(this));
    }

    private void throwConstraintViolationException(Set<String> duplicatedTopics) {
        String message = createConstraintViolationMessage(duplicatedTopics);
        throw new DuplicateReplicationTopicException(message);
    }

    private String createConstraintViolationMessage(Set<String> duplicatedTopics) {
        return VIOLATION_MESSAGE_TEMPLATE.formatted(join(VIOLATION_MESSAGE_TOPICS_DELIMITER, duplicatedTopics));
    }

    static final class DuplicateReplicationTopicException extends RuntimeException {

        public DuplicateReplicationTopicException(String description) {
            super(description);
        }
    }
}
