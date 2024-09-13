package by.aurorasoft.replicator.util;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import lombok.experimental.UtilityClass;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.stream.Stream;

import static javax.lang.model.element.Modifier.PUBLIC;
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
        return element.getModifiers().contains(PUBLIC);
    }

//    public static boolean isReplicatedService(TypeMirror mirror) {
//        return containsSameByClass(mirror.getAnnotationMirrors(), ReplicatedService.class);
//    }

    public static boolean isReplicatedService(Element element) {
        return element.getAnnotation(ReplicatedService.class) != null;
//        return util.getTypeElement(mirror.toString()).asType().getAnnotation(ReplicatedService.class) != null;
    }

    public static boolean isList(TypeMirror mirror, ProcessingEnvironment environment) {
        return environment.getTypeUtils().isAssignable(environment.getTypeUtils().erasure(mirror), environment.getElementUtils().getTypeElement("java.util.List").asType());
    }

    public static boolean isIterable(VariableElement element, ProcessingEnvironment environment) {
        return environment.getTypeUtils().isAssignable(environment.getTypeUtils().erasure(element.asType()), environment.getElementUtils().getTypeElement("java.lang.Iterable").asType());
//        return isSame(element.asType(), Iterable.class);
    }

    public static TypeMirror getFirstGenericParameterType(TypeMirror mirror) {
        return null;
//        return ((DeclaredType) requireNonNull(getTypeElement(mirror))).getTypeArguments().get(0);
    }

    public static TypeMirror getFirstGenericParameterType(VariableElement element) {
        return null;
//        return getFirstGenericParameterType(element.asType());
    }

    public static TypeMirror getFirstTypeParameter(TypeMirror mirror) {
        if (mirror.getKind().isPrimitive() || mirror.getKind() == TypeKind.VOID) {
            throw new RuntimeException();
        }
//        List<? extends TypeParameterElement> typeParameters = ((TypeElement) environment.getTypeUtils().asElement(mirror)).getTypeParameters();
//        if (typeParameters.isEmpty()) {
//            throw new RuntimeException();
//        }
//        return typeParameters.get(0);
        return ((DeclaredType) mirror).getTypeArguments().get(0);
    }

    public static TypeMirror getFirstTypeParameter(VariableElement element) {
        return getFirstTypeParameter(element.asType());
    }

    public static boolean isContainIdGetter(TypeParameterElement typeParameterElement) {
        return typeParameterElement.getGenericElement().getEnclosedElements().stream()
                .filter(element -> element.getKind() == ElementKind.METHOD)
                .filter(element -> element.getSimpleName().contentEquals("getId"))
                .filter(element -> element.getModifiers().contains(PUBLIC))
                .findFirst()
                .isPresent();
    }

    public static boolean isContainIdGetter(TypeMirror typeMirror, ProcessingEnvironment environment) {
        if (typeMirror.getKind().isPrimitive() || typeMirror.getKind() == TypeKind.VOID) {
            return false;
        }
        return environment.getTypeUtils().asElement(typeMirror)
                .getEnclosedElements()
                .stream()
                .filter(enclosedElement -> enclosedElement.getKind() == ElementKind.METHOD)
                .filter(enclosedElement -> enclosedElement.getSimpleName().contentEquals("getId"))
                .filter(enclosedElement -> enclosedElement.getModifiers().contains(PUBLIC))
                .findFirst()
                .isPresent();
    }

    public static boolean isContainIdGetter(VariableElement element, ProcessingEnvironment environment) {
        return isContainIdGetter(element.asType(), environment);
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
