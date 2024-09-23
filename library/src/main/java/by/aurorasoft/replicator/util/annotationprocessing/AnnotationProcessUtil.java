package by.aurorasoft.replicator.util.annotationprocessing;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import lombok.experimental.UtilityClass;
import org.checkerframework.javacutil.ElementUtils;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.stream.Stream;

import static by.aurorasoft.replicator.util.annotationprocessing.ElementUtil.isJpaRepository;
import static by.aurorasoft.replicator.util.annotationprocessing.ElementUtil.isJpaRepositoryField;
import static by.aurorasoft.replicator.util.annotationprocessing.TypeMirrorUtil.isVoid;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Stream.iterate;
import static javax.lang.model.element.ElementKind.FIELD;
import static org.checkerframework.javacutil.TypesUtils.isPrimitive;

@UtilityClass
public final class AnnotationProcessUtil {

    public static <E extends Element> Stream<E> getAnnotatedElements(TypeElement annotation,
                                                                     RoundEnvironment environment,
                                                                     Class<E> elementType) {
        return environment.getElementsAnnotatedWith(annotation)
                .stream()
                .map(elementType::cast);
    }

    //TODO: remove and put into TypeElementUtil and Element replace by TypeElement
    public static boolean isReplicatedService(Element element) {
        return element.getAnnotation(ReplicatedService.class) != null;
    }

    public static boolean isContainIdGetter(TypeMirror mirror, Types typeUtil) {
        return !isVoid(mirror)
                && !isPrimitive(mirror)
                && ElementUtil.isContainIdGetter(typeUtil.asElement(mirror));
    }

    public static boolean isContainIdGetter(TypeParameterElement element) {
        return ElementUtil.isContainIdGetter(element.getGenericElement());
    }

    //TODO: ---------------------------------------------------------------------------
    public static boolean isContainRepository(TypeElement element, Elements elementUtil, Types typeUtil) {
        return iterate(element, e -> !ElementUtils.isObject(e), e -> elementUtil.getTypeElement(typeUtil.erasure(e.getSuperclass()).toString()))
                .flatMap(e -> e.getEnclosedElements().stream())
                .anyMatch(e -> isJpaRepositoryField(e, elementUtil, typeUtil));
    }

    //TODO: refactor
    public static TypeMirror getFirstTypeArgument(Element element) {
        if (element.asType() instanceof DeclaredType declaredType) {
            List<? extends TypeMirror> arguments = declaredType.getTypeArguments();
            if (!arguments.isEmpty()) {
                return arguments.get(0);
            }
        }
        throw new IllegalArgumentException("Impossible to extract first type argument of '%s'".formatted(element));
    }

    //TODO put in DeclaredTypeUtil
    public static TypeMirror getFirstTypeArgument(TypeMirror mirror) {
        if (mirror instanceof DeclaredType declaredType) {
            List<? extends TypeMirror> arguments = declaredType.getTypeArguments();
            if (!arguments.isEmpty()) {
                return arguments.get(0);
            }
        }
        throw new IllegalArgumentException("Impossible to extract first type argument of '%s'".formatted(mirror));
    }

    public static boolean isContainIdGetter(VariableElement element, Elements elementUtil) {
        return ElementUtil.isContainIdGetter(requireNonNull(getTypeElement(element.asType(), elementUtil)));
    }

    public static TypeElement getTypeElement(TypeMirror mirror, Elements elementUtil) {
        return elementUtil.getTypeElement(mirror.toString());
    }
}
