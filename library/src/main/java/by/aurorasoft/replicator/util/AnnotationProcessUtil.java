package by.aurorasoft.replicator.util;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import lombok.experimental.UtilityClass;
import org.checkerframework.javacutil.TypesUtils;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.checkerframework.javacutil.AnnotationUtils.containsSameByClass;
import static org.checkerframework.javacutil.TypesUtils.getClassFromType;
import static org.checkerframework.javacutil.TypesUtils.getTypeElement;

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
        return true;
//        return element.getModifiers().contains(PUBLIC);
    }

    public static boolean isReplicatedService(TypeMirror mirror) {
        return true;
//        return containsSameByClass(mirror.getAnnotationMirrors(), ReplicatedService.class);
    }

    public static boolean isList(TypeMirror mirror) {
        return true;
//        return isSame(mirror, List.class);
    }

    public static boolean isIterable(VariableElement element) {
        return true;
//        return isSame(element.asType(), Iterable.class);
    }

    public static TypeMirror getFirstGenericParameterType(TypeMirror mirror) {
        return ((DeclaredType) requireNonNull(getTypeElement(mirror))).getTypeArguments().get(0);
    }

    public static TypeMirror getFirstGenericParameterType(VariableElement element) {
        return null;
//        return getFirstGenericParameterType(element.asType());
    }

    public static boolean isContainIdGetter(TypeMirror typeMirror) {
        return true;
//        return getTypeElement(typeMirror).getEnclosedElements().stream()
//                .filter(element -> element.getKind() == ElementKind.METHOD)
//                .filter(element -> element.getSimpleName().contentEquals("getId"))
//                .filter(element -> element.getModifiers().contains(PUBLIC))
//                .findFirst().isPresent();
    }

    public static boolean isContainIdGetter(VariableElement element) {
        return isContainIdGetter(element.asType());
    }

    public static boolean isContainRepository(TypeMirror mirror) {
        return true;
//        return getTypeElement(mirror).getEnclosedElements().stream()
//                .filter(element -> element.getKind() == ElementKind.METHOD)
//                .filter(element -> getClassFromType(mirror) == JpaRepository.class)
//                .findFirst().isPresent();
    }

    private static boolean isSame(TypeMirror mirror, Class<?> type) {
        return getClassFromType(mirror) == type;
    }
}
