package by.aurorasoft.replicator.annotation;

import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Stream;

import static javax.lang.model.SourceVersion.latestSupported;
import static javax.tools.Diagnostic.Kind.ERROR;

@AutoService(Processor.class)
public final class ReplicatedServiceProcessor extends AbstractProcessor {
    private static final Class<? extends Annotation> REPLICATED_SERVICE = ReplicatedService.class;
    private static final Class<?> RUD_SERVICE = AbsServiceRUD.class;

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment env) {
        annotations.stream()
                .flatMap(annotation -> getAnnotatedElements(annotation, env))
                .filter(element -> !isRUDService(element))
                .forEach(this::alertWrongAnnotating);
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(getReplicatedServiceName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return latestSupported();
    }

    private static Stream<? extends Element> getAnnotatedElements(final TypeElement annotation,
                                                                  final RoundEnvironment env) {
        return env.getElementsAnnotatedWith(annotation).stream();
    }

    private boolean isRUDService(final Element element) {
        return getTypeUtils().isAssignable(element.asType(), getRawRUDServiceType());
    }

    private TypeMirror getRawRUDServiceType() {
        return getTypeUtils().erasure(getRUDServiceType());
    }

    private TypeMirror getRUDServiceType() {
        return getElementUtils().getTypeElement(getRUDServiceName()).asType();
    }

    private void alertWrongAnnotating(final Element element) {
        getMessager().printMessage(ERROR, getWrongAnnotatingMessage(), element);
    }

    private String getWrongAnnotatingMessage() {
        return "'@%s' can be applied only for subclass of '%s'".formatted(
                getReplicatedServiceName(),
                getRUDServiceName()
        );
    }

    private Types getTypeUtils() {
        return processingEnv.getTypeUtils();
    }

    private Elements getElementUtils() {
        return processingEnv.getElementUtils();
    }

    private Messager getMessager() {
        return processingEnv.getMessager();
    }

    private static String getReplicatedServiceName() {
        return REPLICATED_SERVICE.getName();
    }

    private static String getRUDServiceName() {
        return RUD_SERVICE.getName();
    }
}
