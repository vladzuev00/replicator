package by.aurorasoft.replicator.util.annotationprocessing;

import lombok.experimental.UtilityClass;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.stream.Stream;

@UtilityClass
public final class TypeElementUtil {

    public static <E extends Element> Stream<E> getAnnotatedElements(TypeElement annotation,
                                                                     RoundEnvironment environment,
                                                                     Class<E> elementType) {
        return environment.getElementsAnnotatedWith(annotation)
                .stream()
                .map(elementType::cast);
    }
}
