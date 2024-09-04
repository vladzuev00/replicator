package by.aurorasoft.replicator.util;

import by.aurorasoft.replicator.annotation.processing.error.AnnotationError;
import lombok.experimental.UtilityClass;

import javax.annotation.processing.ProcessingEnvironment;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.joining;
import static javax.tools.Diagnostic.Kind.ERROR;

@UtilityClass
public final class AnnotationErrorAlertUtil {
    private static final String REQUIREMENTS_DELIMITER = "\n\t";
    private static final String ERROR_TEXT_TEMPLATE = "Element annotated by @%s should match next requirements: %s";

    public static void alert(AnnotationError error, ProcessingEnvironment environment) {
        String text = createText(error);
        environment.getMessager().printMessage(ERROR, text, error.getElement());
    }

    private static String createText(AnnotationError error) {
        return error.getRequirements()
                .stream()
                .collect(
                        collectingAndThen(
                                joining(REQUIREMENTS_DELIMITER),
                                requirements -> ERROR_TEXT_TEMPLATE.formatted(
                                        error.getAnnotation().getSimpleName(),
                                        requirements
                                )
                        )
                );
    }
}
