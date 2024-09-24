package by.aurorasoft.replicator.util.annotationprocessing;


import org.junit.jupiter.api.Test;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import java.util.Set;

import static by.aurorasoft.replicator.util.annotationprocessing.TEMPElementUtil.*;
import static javax.lang.model.element.ElementKind.*;
import static javax.lang.model.element.Modifier.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class TEMPElementUtilTest {

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
