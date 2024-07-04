package by.aurorasoft.replicator.annotation.processor;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.nhorushko.crudgeneric.service.RudGenericService;
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
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toCollection;
import static javax.lang.model.SourceVersion.latestSupported;
import static javax.tools.Diagnostic.Kind.ERROR;

@AutoService(Processor.class)
public final class ReplicatedServiceProcessor extends AbstractProcessor {

    @SuppressWarnings("deprecation")
    private static final Set<String> RUD_SERVICE_NAMES = getNames(AbsServiceRUD.class, RudGenericService.class);

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        processInternal(annotations, env);
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(ReplicatedService.class.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return latestSupported();
    }

    private static Set<String> getNames(Class<?>... classes) {
        return stream(classes)
                .map(Class::getName)
                .collect(toCollection(LinkedHashSet::new));
    }

    private void processInternal(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        annotations.stream()
                .flatMap(annotation -> getAnnotatedElements(annotation, env))
                .filter(element -> !isRUDService(element))
                .forEach(this::alertWrongAnnotating);
    }

    private Stream<? extends Element> getAnnotatedElements(TypeElement annotation, RoundEnvironment env) {
        return env.getElementsAnnotatedWith(annotation).stream();
    }

    private boolean isRUDService(Element element) {
        return RUD_SERVICE_NAMES.stream()
                .map(this::getRawType)
                .anyMatch(rawServiceType -> isInstanceOf(element, rawServiceType));
    }

    private TypeMirror getRawType(String name) {
        return getTypeUtils().erasure(getType(name));
    }

    private TypeMirror getType(String name) {
        return getElementUtils()
                .getTypeElement(name)
                .asType();
    }

    private boolean isInstanceOf(Element element, TypeMirror type) {
        return getTypeUtils().isAssignable(element.asType(), type);
    }

    private void alertWrongAnnotating(Element element) {
        getMessager().printMessage(ERROR, getWrongAnnotatingMessage(), element);
    }

    private String getWrongAnnotatingMessage() {
        return "'@%s' can be applied only for subclass one of '%s'".formatted(
                ReplicatedService.class.getName(),
                RUD_SERVICE_NAMES
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
}
