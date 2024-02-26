package by.aurorasoft.replicator.annotation;

import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Stream;

import static javax.lang.model.SourceVersion.latestSupported;
import static javax.tools.Diagnostic.Kind.ERROR;
import static org.checkerframework.javacutil.ElementUtils.getAllSupertypes;

@AutoService(Processor.class)
public final class ReplicatedServiceProcessor extends AbstractProcessor {
    private static final Class<? extends Annotation> ANNOTATION = ReplicatedService.class;

    @Override
    public boolean process(final Set<? extends TypeElement> handledAnnotations, final RoundEnvironment env) {
        handledAnnotations.stream()
                .flatMap(annotation -> getAnnotatedElements(annotation, env))
                .filter(element -> !isRUDServiceInheritor(element))
                .forEach(this::alertWrongAnnotating);
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(ANNOTATION.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return latestSupported();
    }

    private static Stream<? extends Element> getAnnotatedElements(final TypeElement annotation,
                                                                  final RoundEnvironment env) {
        return env.getElementsAnnotatedWith(annotation).stream();
    }

    private boolean isRUDServiceInheritor(final Element element) {
        return (element instanceof final TypeElement typeElement) && isRUDServiceInheritor(typeElement);
    }

    private boolean isRUDServiceInheritor(final TypeElement element) {
        return getAllSupertypes(element, processingEnv).stream().anyMatch(ReplicatedServiceProcessor::isRUDService);
    }

    private static boolean isRUDService(final TypeElement element) {
        return element.getQualifiedName().contentEquals(AbsServiceRUD.class.getName());
    }

    private void alertWrongAnnotating(final Element element) {
        processingEnv.getMessager().printMessage(ERROR, getWrongAnnotatingMessage(), element);
    }

    private String getWrongAnnotatingMessage() {
        return "Annotation '%s' can be applied only for subclasses of '%s'".formatted(
                ANNOTATION.getSimpleName(),
                AbsServiceRUD.class
        );
    }
}
