package by.aurorasoft.replicator.util.annotationprocessing;

import lombok.experimental.UtilityClass;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.NoSuchElementException;

import static by.aurorasoft.replicator.util.annotationprocessing.TEMPElementUtil.isPackage;
import static by.aurorasoft.replicator.util.annotationprocessing.TEMPElementUtil.isPublic;
import static java.util.stream.Stream.iterate;
import static javax.lang.model.element.ElementKind.METHOD;
import static org.checkerframework.javacutil.ElementUtils.isStatic;

@UtilityClass
public final class ExecutableElementUtil {
    static final String ID_GETTER_NAME = "getId";

    public static boolean isIdGetter(ExecutableElement element) {
        return element.getKind() == METHOD
                && isPublic(element)
                && !isStatic(element)
                && element.getSimpleName().contentEquals(ID_GETTER_NAME)
                && element.getParameters().isEmpty();
    }

    public static TypeElement getEnclosingClass(ExecutableElement element) {
        return (TypeElement) iterate(element, e -> !isPackage(e), Element::getEnclosingElement)
                .filter(TEMPElementUtil::isClass)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No enclosing class of '%s'".formatted(element)));
    }
}
