package by.aurorasoft.replicator.util;

import lombok.experimental.UtilityClass;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

import static javax.lang.model.element.Modifier.PUBLIC;

@UtilityClass
public final class ElementUtil {

    public static boolean isContainIdGetter(Element element) {
        return element.getEnclosedElements()
                .stream()
                .filter(enclosedElement -> enclosedElement.getKind() == ElementKind.METHOD)
                .filter(enclosedElement -> enclosedElement.getSimpleName().contentEquals("getId"))
                .filter(enclosedElement -> enclosedElement.getModifiers().contains(PUBLIC))
                .findFirst()
                .isPresent();
    }
}
