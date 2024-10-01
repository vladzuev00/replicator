package by.aurorasoft.replicator.util.annotationprocessing;

import lombok.experimental.UtilityClass;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.stream.Stream;

import static by.aurorasoft.replicator.util.annotationprocessing.ElementUtil.isJpaRepositoryField;
import static by.aurorasoft.replicator.util.annotationprocessing.TypeMirrorUtil.getErasuredTypeElement;
import static java.util.stream.Stream.iterate;
import static org.checkerframework.javacutil.ElementUtils.isObject;

@UtilityClass
public final class TypeElementUtil {

    public static <E extends Element> Stream<E> getAnnotatedElements(TypeElement annotation,
                                                                     RoundEnvironment environment,
                                                                     Class<E> elementType) {
        return environment.getElementsAnnotatedWith(annotation)
                .stream()
                .map(elementType::cast);
    }

    public static boolean isContainRepository(TypeElement element, Elements elementUtil, Types typeUtil) {
        return iterate(element, e -> !isObject(e), e -> getErasuredTypeElement(e.getSuperclass(), elementUtil, typeUtil))
                .flatMap(e -> e.getEnclosedElements().stream())
                .filter(e -> e instanceof VariableElement)
                .map(e -> (VariableElement) e)
                .anyMatch(e -> isJpaRepositoryField(e, elementUtil, typeUtil));
    }

    public static boolean isContainIdGetter(TypeElement element, Elements elementUtil, Types typeUtil) {
        return iterate(element, e -> !isObject(e), e -> getErasuredTypeElement(e.getSuperclass(), elementUtil, typeUtil))
                .flatMap(e -> e.getEnclosedElements().stream())
                .filter(e -> e instanceof ExecutableElement)
                .map(e -> (ExecutableElement) e)
                .anyMatch(ExecutableElementUtil::isIdGetter);
    }
}
