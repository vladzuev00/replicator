package by.aurorasoft.replicator.util.annotationprocessing;

import by.aurorasoft.replicator.util.NameImpl;
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

            TypeElement firstGivenSuperClass = mockErasuredSuperClass(
                    givenElement,
                    "FirstSuperClass",
                    mockedElementUtil,
                    givenElementUtil,
                    givenTypeUtil
            );
            TypeElement secondGivenSuperClass = mockErasuredSuperClass(
                    firstGivenSuperClass,
                    "SecondSuperClass",
                    mockedElementUtil,
                    givenElementUtil,
                    givenTypeUtil
            );


        }
    }

    private TypeElement mockErasuredSuperClass(TypeElement element,
                                               String superClassName,
                                               boolean containRepository,
                                               MockedStatic<ElementUtil> mockedElementUtil,
                                               Elements elementUtil,
                                               Types typeUtil) {
        TypeMirror superClassMirror = mock(TypeMirror.class);
        when(element.getSuperclass()).thenReturn(superClassMirror);
        TypeElement erasuredSuperClass = mock(TypeElement.class);
        when(erasuredSuperClass.getQualifiedName()).thenReturn(new NameImpl(superClassName));
        mockedElementUtil.when(() -> getErasuredTypeElement(same(superClassMirror), same(elementUtil), same(typeUtil)))
                .thenReturn(erasuredSuperClass);
        mockedElementUtil.when(() -> )
        return erasuredSuperClass;
    }
}
