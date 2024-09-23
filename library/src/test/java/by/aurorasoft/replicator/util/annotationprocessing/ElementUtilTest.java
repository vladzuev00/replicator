package by.aurorasoft.replicator.util.annotationprocessing;


import org.junit.jupiter.api.Test;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import java.util.Set;

import static by.aurorasoft.replicator.util.annotationprocessing.ElementUtil.*;
import static javax.lang.model.element.ElementKind.*;
import static javax.lang.model.element.Modifier.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ElementUtilTest {

    @Test
    public void elementShouldBePublic() {
        Element givenElement = mock(Element.class);

        Set<Modifier> givenModifiers = Set.of(NATIVE, SYNCHRONIZED, PUBLIC, VOLATILE);
        when(givenElement.getModifiers()).thenReturn(givenModifiers);

        assertTrue(isPublic(givenElement));
    }

    @Test
    public void elementShouldNotBePublic() {
        Element givenElement = mock(Element.class);

        Set<Modifier> givenModifiers = Set.of(NATIVE, SYNCHRONIZED, PRIVATE, VOLATILE);
        when(givenElement.getModifiers()).thenReturn(givenModifiers);

        assertFalse(isPublic(givenElement));
    }

    @Test
    public void elementShouldBeClass() {
        Element givenElement = mock(Element.class);

        when(givenElement.getKind()).thenReturn(CLASS);

        assertTrue(isClass(givenElement));
    }

    @Test
    public void elementShouldNotBeClass() {
        Element givenElement = mock(Element.class);

        when(givenElement.getKind()).thenReturn(INTERFACE);

        assertFalse(isClass(givenElement));
    }

    @Test
    public void elementShouldBePackage() {
        Element givenElement = mock(Element.class);

        when(givenElement.getKind()).thenReturn(PACKAGE);

        assertTrue(isPackage(givenElement));
    }

    @Test
    public void elementShouldNotBePackage() {
        Element givenElement = mock(Element.class);

        when(givenElement.getKind()).thenReturn(CLASS);

        assertFalse(isPackage(givenElement));
    }
}
