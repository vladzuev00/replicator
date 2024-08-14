package by.aurorasoft.replicator.annotation.processor;

import lombok.RequiredArgsConstructor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Set;

import static java.lang.String.join;
import static javax.lang.model.SourceVersion.latestSupported;
import static javax.tools.Diagnostic.Kind.ERROR;

@RequiredArgsConstructor
public abstract class ReplicateAnnotationProcessor extends AbstractProcessor {
    private static final String ERROR_MESSAGE_TEMPLATE = "Elements annotated by @%s should meet next requirements: %s";
    private static final String REQUIREMENTS_DELIMITER = "\n\t";

    private final Class<Annotation> annotation;

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

    protected abstract Set<String> getRequirements();

    private void alertError(Element element) {
        processingEnv.getMessager().printMessage(ERROR, getErrorMessage(), element);
    }

    private String getErrorMessage() {
        String requirements = join(REQUIREMENTS_DELIMITER, getRequirements());
        return ERROR_MESSAGE_TEMPLATE.formatted(annotation.getName(), requirements);
    }
}
