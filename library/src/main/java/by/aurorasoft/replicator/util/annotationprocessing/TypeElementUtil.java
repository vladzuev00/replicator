package by.aurorasoft.replicator.util.annotationprocessing;

import lombok.experimental.UtilityClass;
import org.checkerframework.javacutil.ElementUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.stream.Stream;

import static by.aurorasoft.replicator.util.annotationprocessing.ElementUtil.isJpaRepositoryField;
import static by.aurorasoft.replicator.util.annotationprocessing.TypeMirrorUtil.getErasuredTypeElement;
import static java.util.stream.Stream.iterate;

@UtilityClass
public final class TypeElementUtil {

    public static <E extends Element> Stream<E> getAnnotatedElements(TypeElement annotation,
                                                                     RoundEnvironment environment,
                                                                     Class<E> elementType) {
        return environment.getElementsAnnotatedWith(annotation)
                .stream()
                .map(elementType::cast);
    }

    //TODO: test
    public static boolean isContainRepository(TypeElement element, Elements elementUtil, Types typeUtil) {
        return iterate(element, e -> !ElementUtils.isObject(e), e -> getErasuredTypeElement(e.getSuperclass(), elementUtil, typeUtil))
                .flatMap(e -> e.getEnclosedElements().stream())
                .anyMatch(e -> isJpaRepositoryField(e, elementUtil, typeUtil));
    }
}
