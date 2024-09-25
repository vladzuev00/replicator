package by.aurorasoft.replicator.util.annotationprocessing;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static by.aurorasoft.replicator.util.annotationprocessing.TypeElementUtil.getAnnotatedElements;
import static by.aurorasoft.replicator.util.annotationprocessing.TypeMirrorUtil.getErasuredTypeElement;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public final class TypeElementUtilTest {

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void annotatedElementsShouldBeGot() {
        TypeElement givenAnnotation = mock(TypeElement.class);
        RoundEnvironment givenEnvironment = mock(RoundEnvironment.class);
        Class<ExecutableElement> givenElementType = ExecutableElement.class;

        Element firstGivenElement = mock(ExecutableElement.class);
        Element secondGivenElement = mock(ExecutableElement.class);
        Element thirdGivenElement = mock(ExecutableElement.class);
        Set givenElements = new LinkedHashSet<>(List.of(firstGivenElement, secondGivenElement, thirdGivenElement));
        when(givenEnvironment.getElementsAnnotatedWith(same(givenAnnotation))).thenReturn(givenElements);

        var actual = getAnnotatedElements(givenAnnotation, givenEnvironment, givenElementType).toList();
        List<Element> expected = List.of(firstGivenElement, secondGivenElement, thirdGivenElement);
        assertEquals(expected, actual);
    }

    @Test
    public void elementShouldContainRepository() {
        try (MockedStatic<TypeMirrorUtil> mockedTypeMirrorUtil = mockStatic(TypeMirrorUtil.class);
             MockedStatic<ElementUtil> mockedElementUtil = mockStatic(ElementUtil.class)) {
            TypeElement givenElement = mock(TypeElement.class);
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            var firstGivenSuperType = mockSuperClass(givenElement, mockedElementUtil, givenElementUtil, givenTypeUtil);
            throw new RuntimeException();
        }
    }

    private TypeElement mockSuperClass(TypeElement element,
                                       MockedStatic<ElementUtil> mockedElementUtil,
                                       Elements elementUtil,
                                       Types typeUtil) {
        TypeMirror superClassMirror = mock(TypeMirror.class);
        TypeElement superClass = mock(TypeElement.class);
        when(element.getSuperclass()).thenReturn(superClassMirror);
        mockedElementUtil.when(
                () -> getErasuredTypeElement(
                        same(superClassMirror),
                        same(elementUtil),
                        same(typeUtil)
                )
        ).thenReturn(superClass);
        return superClass;
    }
}
