package by.aurorasoft.replicator.util.annotationprocessing;

import by.aurorasoft.replicator.annotation.processing.error.AnnotationProcessError;
import lombok.experimental.UtilityClass;

import javax.annotation.processing.ProcessingEnvironment;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.joining;
import static javax.tools.Diagnostic.Kind.ERROR;

@UtilityClass
public final class AnnotationProcessErrorAlertUtil {
    private static final String REQUIREMENT_PREFIX = "\n\t - ";
    private static final String MESSAGE_TEMPLATE = "Element annotated by @%s should match next requirements:%s";

    public static void alert(AnnotationProcessError error, ProcessingEnvironment environment) {
        environment.getMessager().printMessage(ERROR, getMessage(error), error.getElement());
    }

    private static String getMessage(AnnotationProcessError error) {
        return error.getRequirements()
                .stream()
                .map(requirement -> REQUIREMENT_PREFIX + requirement)
                .collect(
                        collectingAndThen(
                                joining(),
                                requirements -> MESSAGE_TEMPLATE.formatted(
                                        error.getAnnotation().getSimpleName(),
                                        requirements
                                )
                        )
                );
    }
}
