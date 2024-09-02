package by.aurorasoft.replicator.annotation.processor;

import lombok.RequiredArgsConstructor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.joining;
import static javax.lang.model.SourceVersion.latestSupported;
import static javax.tools.Diagnostic.Kind.ERROR;

@RequiredArgsConstructor
public abstract class ReplicaAnnotationProcessor<E extends Element> extends AbstractProcessor {
    private final Class<? extends Annotation> annotation;
    private final Class<E> elementType;

    @Override
    public final boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        annotations.stream()
                .flatMap(annotation -> getAnnotatedElements(annotation, env))
                .filter(element -> !isValid(element))
                .forEach(this::alertError);
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

    protected abstract boolean isValid(E element);

    protected abstract Set<String> getRequirements();

    private Stream<E> getAnnotatedElements(TypeElement annotation, RoundEnvironment env) {
        return env.getElementsAnnotatedWith(annotation)
                .stream()
                .map(elementType::cast);
    }

    private void alertError(E element) {
        ErrorMessage message = new ErrorMessage(annotation, getRequirements());
        processingEnv.getMessager().printMessage(ERROR, message.getText(), element);
    }

    @RequiredArgsConstructor
    static final class ErrorMessage {
        private static final String TEXT_TEMPLATE = "Element annotated by @%s should match next requirements: %s";
        private static final String REQUIREMENTS_DELIMITER = "\n\t";

        private final Class<? extends Annotation> annotation;
        private final Set<String> requirements;

        public String getText() {
            return requirements.stream()
                    .collect(
                            collectingAndThen(
                                    joining(REQUIREMENTS_DELIMITER),
                                    requirements -> TEXT_TEMPLATE.formatted(annotation.getSimpleName(), requirements)
                            )
                    );
        }
    }
}
