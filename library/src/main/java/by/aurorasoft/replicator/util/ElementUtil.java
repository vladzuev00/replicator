package by.aurorasoft.replicator.util;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import lombok.experimental.UtilityClass;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;

import java.util.stream.Stream;

import static javax.lang.model.element.Modifier.PUBLIC;

@UtilityClass
public final class ElementUtil {

    public static <E extends Element> Stream<E> getAnnotatedElements(TypeElement annotation,
                                                                     RoundEnvironment environment,
                                                                     Class<E> elementType) {
        return environment.getElementsAnnotatedWith(annotation)
                .stream()
                .map(elementType::cast);
    }

    public static boolean isContainIdGetter(Element element) {
        return element.getEnclosedElements()
                .stream()
                .filter(enclosedElement -> enclosedElement.getKind() == ElementKind.METHOD)
                .filter(enclosedElement -> enclosedElement.getSimpleName().contentEquals("getId"))
                .filter(enclosedElement -> enclosedElement.getModifiers().contains(PUBLIC))
                .findFirst()
                .isPresent();
    }

    public static boolean isPublic(Element element) {
        return element.getModifiers().contains(PUBLIC);
    }

    public static boolean isReplicatedService(Element element) {
        return element.getAnnotation(ReplicatedService.class) != null;
    }

    public static boolean isIterable(Element element, ProcessingEnvironment environment) {
        return TypeMirrorUtil.isIterable(element.asType(), environment);
    }

    public static TypeMirror getFirstTypeArgument(Element element) {
        return TypeMirrorUtil.getFirstTypeArgument(element.asType());
    }

    public static boolean isContainIdGetter(TypeParameterElement typeParameterElement) {
        return typeParameterElement.getGenericElement().getEnclosedElements().stream()
                .filter(element -> element.getKind() == ElementKind.METHOD)
                .filter(element -> element.getSimpleName().contentEquals("getId"))
                .filter(element -> element.getModifiers().contains(PUBLIC))
                .findFirst()
                .isPresent();
    }
}
