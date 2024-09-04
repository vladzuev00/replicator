package by.aurorasoft.replicator.util;

import by.aurorasoft.replicator.annotation.processing.error.AnnotationError;
import lombok.experimental.UtilityClass;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.joining;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.tools.Diagnostic.Kind.ERROR;

@UtilityClass
public final class AnnotationProcessingUtil {
    private static final String REQUIREMENTS_DELIMITER = "\n\t";
    private static final String TEXT_TEMPLATE = "Element annotated by @%s should match next requirements: %s";

    public static <E extends Element> Stream<E> getAnnotatedElements(TypeElement annotation,
                                                                     RoundEnvironment environment,
                                                                     Class<E> elementType) {
        return environment.getElementsAnnotatedWith(annotation)
                .stream()
                .map(elementType::cast);
    }

    public static boolean isPublic(Element element) {
        return element.getModifiers().contains(PUBLIC);
    }

    public static void alert(AnnotationError error, ProcessingEnvironment environment) {
        String text = error.getRequirements()
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
        environment.getMessager().printMessage(ERROR, text, error.getElement());
    }
}
