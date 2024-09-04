package by.aurorasoft.replicator.annotation.processing;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Stream;

import static by.aurorasoft.replicator.util.AnnotationProcessingUtil.getAnnotatedElements;
import static by.aurorasoft.replicator.util.AnnotationProcessingUtil.isPublic;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.concat;
import static javax.lang.model.SourceVersion.latestSupported;
import static javax.tools.Diagnostic.Kind.ERROR;

@RequiredArgsConstructor
public abstract class ReplicaAnnotationProcessor<E extends Element> extends AbstractProcessor {
    private static final String PUBLIC_MODIFIER_REQUIREMENT = "It should be public";
    private static final String REQUIREMENTS_DELIMITER = "\n\t";

    private final Class<? extends Annotation> annotation;
    private final Class<E> elementType;

    @Override
    public final boolean process(Set<? extends TypeElement> annotations, RoundEnvironment environment) {
        annotations.stream()
                .flatMap(annotation -> getAnnotatedElements(annotation, environment, elementType))
                .filter(element -> !isValid(element))
                .map(this::createErrorMessage)
                .forEach(this::alert);
        return true;
    }

    @Override
    public final Set<String> getSupportedAnnotationTypes() {
        return Set.of(annotation.getName());
    }

    @Override
    public final SourceVersion getSupportedSourceVersion() {
        return latestSupported();
    }

    protected abstract boolean isValidPublicElement(E element);

    protected abstract Stream<String> getRequirementsInternal();

    private boolean isValid(E element) {
        return isPublic(element) && isValidPublicElement(element);
    }

    private ErrorMessage createErrorMessage(E element) {
        return concat(Stream.of(PUBLIC_MODIFIER_REQUIREMENT), getRequirementsInternal())
                .collect(
                        collectingAndThen(
                                joining(REQUIREMENTS_DELIMITER),
                                requirements -> new ErrorMessage(element, annotation, requirements)
                        )
                );
    }

    private void alert(ErrorMessage message) {
        processingEnv.getMessager().printMessage(ERROR, message.getText(), message.getElement());
    }

//    @RequiredArgsConstructor
//    static final class ErrorMessage {
//        private static final String TEXT_TEMPLATE = "Element annotated by @%s should match next requirements: %s";
//        private static final String REQUIREMENTS_DELIMITER = "\n\t";
//
//        private final Element element;
//        private final Class<? extends Annotation> annotation;
//        private final Set<String> requirements;
//
//        public String getText() {
//            return requirements.stream()
//                    .collect(
//                            collectingAndThen(
//                                    joining(REQUIREMENTS_DELIMITER),
//                                    requirements -> TEXT_TEMPLATE.formatted(annotation.getSimpleName(), requirements)
//                            )
//                    );
//        }
//    }
}
