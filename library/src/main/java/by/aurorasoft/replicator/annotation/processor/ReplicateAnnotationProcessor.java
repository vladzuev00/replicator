package by.aurorasoft.replicator.annotation.processor;

import lombok.RequiredArgsConstructor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Set;

import static javax.lang.model.SourceVersion.latestSupported;
import static javax.tools.Diagnostic.Kind.ERROR;

@RequiredArgsConstructor
public abstract class ReplicateAnnotationProcessor extends AbstractProcessor {
    private final Class<Annotation> annotation;
    private final String errorMessage;

    @Override
    public final boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        annotations.stream()
                .flatMap(annotation -> env.getElementsAnnotatedWith(annotation).stream())
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

    protected abstract boolean isValid(Element element);

    private void alertError(Element element) {
        processingEnv.getMessager().printMessage(ERROR, errorMessage, element);
    }
}
