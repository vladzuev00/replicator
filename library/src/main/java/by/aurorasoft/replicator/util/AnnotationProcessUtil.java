package by.aurorasoft.replicator.util;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import lombok.experimental.UtilityClass;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.checkerframework.javacutil.TypesUtils.getClassFromType;

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

    public static boolean isIterable(VariableElement element, ProcessingEnvironment environment) {
        return TypeMirrorUtil.isIterable(element.asType(), environment);
//        return isSame(element.asType(), Iterable.class);
    }

    public static TypeMirror getFirstTypeParameter(VariableElement element) {
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

    public static boolean isContainRepository(TypeMirror mirror, ProcessingEnvironment environment) {
        return concat(environment.getTypeUtils().directSupertypes(mirror).stream(), Stream.of(mirror)).flatMap(superType -> environment.getTypeUtils().asElement(superType).getEnclosedElements().stream())
                .filter(enclosedElement -> enclosedElement.getKind() == ElementKind.FIELD)
                .filter(enclosedElement -> enclosedElement.getSimpleName().contentEquals("repository"))
                .filter(enclosedElement -> isRepository(enclosedElement, environment))
                .findFirst()
                .isPresent();
    }

    private static List<TypeMirror> getAllSuperTypes(TypeMirror mirror, ProcessingEnvironment environment) {
        return List.of();
//        List<TypeMirror> superTypes = new ArrayList<>();
//        List<? extends TypeMirror> current = environment.getTypeUtils().directSupertypes(mirror);
//        do {
//            superTypes.addAll(current);
//            current.clear();
//            current = environment.getTypeUtils().directSupertypes(m)
//        }while (environment.getTypeUtils().isSameType(current.get(0), environment.getElementUtils().getTypeElement("java.lang.Object").asType()));
    }

    private static boolean isRepository(Element element, ProcessingEnvironment environment) {
        return environment.getTypeUtils().isAssignable(environment.getTypeUtils().erasure(element.asType()), environment.getElementUtils().getTypeElement("org.springframework.data.jpa.repository.JpaRepository").asType());
    }

    private static boolean isSame(TypeMirror mirror, Class<?> type) {
        return getClassFromType(mirror) == type;
    }
}
