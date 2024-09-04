package by.aurorasoft.replicator.util;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import lombok.experimental.UtilityClass;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.checkerframework.javacutil.AnnotationUtils.containsSameByClass;

@UtilityClass
public final class AnnotationProcessUtil {

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

    public static boolean isReplicatedService(TypeMirror mirror) {
        return containsSameByClass(mirror.getAnnotationMirrors(), ReplicatedService.class);
    }

    public static boolean isIterable(VariableElement mirror) {
        return true;
//        return TypesUtils.getClassFromType(elements.get(0).asType()).isAssignableFrom(Iterable.class);
    }
}
