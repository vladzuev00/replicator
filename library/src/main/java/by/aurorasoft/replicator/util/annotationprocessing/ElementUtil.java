package by.aurorasoft.replicator.util.annotationprocessing;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import lombok.experimental.UtilityClass;
import org.checkerframework.javacutil.ElementUtils;
import org.checkerframework.javacutil.TypesUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.stream.Stream;

import static javax.lang.model.element.ElementKind.METHOD;
import static javax.lang.model.element.Modifier.PUBLIC;

@UtilityClass
public final class ElementUtil {
    static final String GETTER_NAME_ID = "getId";

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

    public static boolean isReplicatedService(Element element) {
        return element.getAnnotation(ReplicatedService.class) != null;
    }

    public static boolean isIterable(Element element, ProcessingEnvironment environment) {
        return TypeMirrorUtil.isIterable(element.asType(), environment);
    }

    public static TypeMirror getFirstTypeArgument(Element element, ProcessingEnvironment environment) {
        return TypeMirrorUtil.getFirstTypeArgument(element.asType());
    }

    public static boolean isContainRepository(TypeElement mirror, ProcessingEnvironment environment) {
//TODO        ElementUtils.getAllSupertypes()
        return ElementUtil.getInheritance(mirror, environment)
                .flatMap(e -> e.getEnclosedElements().stream())
                .filter(enclosedElement -> enclosedElement.getKind() == ElementKind.FIELD)
                .filter(enclosedElement -> enclosedElement.getSimpleName().contentEquals("repository"))
                .map(Element::asType)
                .filter(type -> isJpaRepository(type, environment))
                .findFirst()
                .isPresent();
    }

    public static boolean isJpaRepository(TypeMirror mirror, ProcessingEnvironment environment) {
        return isErasedSubtype(mirror, "org.springframework.data.jpa.repository.JpaRepository", environment);
    }

    //TODO: check static, check parameters
    public static boolean isIdGetter(Element element) {
        return element.getKind() == METHOD
                && isPublic(element)
                && element.getSimpleName().contentEquals(GETTER_NAME_ID);
    }

    //TODO: ElementUtils.matchesElement
    public static boolean isContainIdGetter(Element element) {
        return element.getEnclosedElements()
                .stream()
                .anyMatch(ElementUtil::isIdGetter);
    }

    //TODO: temp
    public static boolean isContainIdGetter(VariableElement element, ProcessingEnvironment environment) {
        return isContainIdGetter(environment.getElementUtils().getTypeElement(element.asType().toString()));
    }

    public static boolean isContainIdGetter(TypeParameterElement element) {
        return isContainIdGetter(element.getGenericElement());
    }

    //TODO: temp
    public static Stream<TypeElement> getInheritance(TypeElement element, ProcessingEnvironment environment) {
        return Stream.iterate(element, e -> environment.getElementUtils().getTypeElement(environment.getTypeUtils().erasure(e.getSuperclass()).toString()) != null, e -> environment.getElementUtils().getTypeElement(environment.getTypeUtils().erasure(e.getSuperclass()).toString()));
    }

    private static boolean isErasedSubtype(TypeMirror subtype, String supertypeName, ProcessingEnvironment environment) {
        TypeMirror supertype = environment.getElementUtils()
                .getTypeElement(supertypeName)
                .asType();
        return TypesUtils.isErasedSubtype(subtype, supertype, environment.getTypeUtils());
    }
}
