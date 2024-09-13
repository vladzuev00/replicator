package by.aurorasoft.replicator.annotation.processing.processor;

import by.aurorasoft.replicator.annotation.processing.error.AnnotationProcessError;
import lombok.RequiredArgsConstructor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

import static by.aurorasoft.replicator.util.AnnotationProcessErrorAlertUtil.alert;
import static by.aurorasoft.replicator.util.AnnotationProcessUtil.getAnnotatedElements;
import static by.aurorasoft.replicator.util.AnnotationProcessUtil.isPublic;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Stream.concat;
import static javax.lang.model.SourceVersion.latestSupported;

@RequiredArgsConstructor
public abstract class ReplicaAnnotationProcessor<E extends Element> extends AbstractProcessor {
    private static final String PUBLIC_MODIFIER_REQUIREMENT = "Element should be public";

    private final Class<? extends Annotation> annotation;
    private final Class<E> elementType;

    @Override
    public final boolean process(Set<? extends TypeElement> annotations, RoundEnvironment environment) {
        annotations.stream()
                .flatMap(annotation -> getAnnotatedElements(annotation, environment, elementType))
                .filter(element -> !isValid(element))
                .map(this::createError)
                .forEach(error -> alert(error, processingEnv));
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

    private AnnotationProcessError createError(E element) {
        return concat(Stream.of(PUBLIC_MODIFIER_REQUIREMENT), getRequirementsInternal())
                .collect(
                        collectingAndThen(
                                toCollection(LinkedHashSet::new),
                                requirements -> new AnnotationProcessError(element, annotation, requirements)
                        )
                );
    }
}
