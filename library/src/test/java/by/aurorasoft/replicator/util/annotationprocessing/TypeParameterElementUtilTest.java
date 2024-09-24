package by.aurorasoft.replicator.util.annotationprocessing;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeParameterElement;

import static by.aurorasoft.replicator.util.annotationprocessing.TypeParameterElementUtil.isContainIdGetter;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public final class TypeParameterElementUtilTest {

    @Test
    public void elementShouldContainIdGetter() {
        try (MockedStatic<ElementUtil> mockedElementUtil = mockStatic(ElementUtil.class)) {
            TypeParameterElement givenElement = mock(TypeParameterElement.class);

            Element givenGenericElement = mock(Element.class);
            when(givenElement.getGenericElement()).thenReturn(givenGenericElement);

            mockedElementUtil.when(() -> ElementUtil.isContainIdGetter(same(givenGenericElement))).thenReturn(true);

            assertTrue(isContainIdGetter(givenElement));
        }
    }

    @Test
    public void elementShouldNotContainIdGetter() {
        try (MockedStatic<ElementUtil> mockedElementUtil = mockStatic(ElementUtil.class)) {
            TypeParameterElement givenElement = mock(TypeParameterElement.class);

            Element givenGenericElement = mock(Element.class);
            when(givenElement.getGenericElement()).thenReturn(givenGenericElement);

            mockedElementUtil.when(() -> ElementUtil.isContainIdGetter(same(givenGenericElement))).thenReturn(false);

            assertFalse(isContainIdGetter(givenElement));
        }
    }
}
