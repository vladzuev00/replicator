package by.aurorasoft.replicator.util.annotationprocessing;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import lombok.experimental.UtilityClass;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static javax.lang.model.element.ElementKind.*;
import static javax.lang.model.element.Modifier.PUBLIC;

@UtilityClass
public final class ElementUtil {
    static final String JPA_REPOSITORY_FIELD_NAME = "repository";

    public static boolean isPublic(Element element) {
        return element.getModifiers().contains(PUBLIC);
    }

    public static boolean isClass(Element element) {
        return element.getKind() == CLASS;
    }

    public static boolean isPackage(Element element) {
        return element.getKind() == PACKAGE;
    }

    public static boolean isReplicatedService(Element element) {
        return element.getAnnotation(ReplicatedService.class) != null;
    }

    public static boolean isIterable(Element element, Elements elementUtil, Types typeUtil) {
        return TypeMirrorUtil.isIterable(element.asType(), elementUtil, typeUtil);
    }

    public static boolean isJpaRepository(Element element, Elements elementUtil, Types typeUtil) {
        return TypeMirrorUtil.isJpaRepository(element.asType(), elementUtil, typeUtil);
    }

    public static boolean isContainIdGetter(Element element) {
        return element.getEnclosedElements()
                .stream()
                .filter(e -> e instanceof ExecutableElement)
                .map(e -> (ExecutableElement) e)
                .anyMatch(ExecutableElementUtil::isIdGetter);
    }

    public static boolean isJpaRepositoryField(Element element, Elements elementUtil, Types typeUtil) {
        return element.getKind() == FIELD
                && element.getSimpleName().contentEquals(JPA_REPOSITORY_FIELD_NAME)
                && isJpaRepository(element, elementUtil, typeUtil);
    }

    public static TypeMirror getFirstTypeArgument(Element element) {
        return DeclaredTypeUtil.getFirstTypeArgument((DeclaredType) element.asType());
    }

    //TODO: test, VariableElementUtil
    public static TypeElement getTypeElement(VariableElement element, Elements elementUtil) {
        return elementUtil.getTypeElement(element.asType().toString());
    }

    //TODO: test, TypeMirrorUtil
    public static TypeElement getErasuredTypeElement(TypeMirror mirror, Elements elementUtil, Types typeUtil) {
        return elementUtil.getTypeElement(typeUtil.erasure(mirror).toString());
    }
}
