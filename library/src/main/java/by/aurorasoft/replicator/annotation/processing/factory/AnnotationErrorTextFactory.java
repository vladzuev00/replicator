package by.aurorasoft.replicator.annotation.processing.factory;

import by.aurorasoft.replicator.annotation.processing.error.AnnotationError;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.joining;

public final class AnnotationErrorTextFactory {
    private static final String REQUIREMENTS_DELIMITER = "\n\t";
    private static final String TEXT_TEMPLATE = "Element annotated by @%s should match next requirements: %s";

    public String create(AnnotationError error) {
        return error.getRequirements()
                .stream()
                .collect(
                        collectingAndThen(
                                joining(REQUIREMENTS_DELIMITER),
                                requirements -> TEXT_TEMPLATE.formatted(
                                        error.getAnnotation().getSimpleName(),
                                        requirements
                                )
                        )
                );
    }
}
