package by.aurorasoft.replicator.util.annotationprocessing;

import by.aurorasoft.replicator.util.NameImpl;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static by.aurorasoft.replicator.util.annotationprocessing.TypeElementUtil.getAnnotatedElements;
import static by.aurorasoft.replicator.util.annotationprocessing.TypeElementUtil.isContainRepository;
import static by.aurorasoft.replicator.util.annotationprocessing.TypeMirrorUtil.getErasuredTypeElement;
import static by.aurorasoft.replicator.util.annotationprocessing.VariableElementUtil.isJpaRepositoryField;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
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
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void elementShouldContainRepository() {
        try (MockedStatic<TypeMirrorUtil> mockedTypeMirrorUtil = mockStatic(TypeMirrorUtil.class);
             MockedStatic<VariableElementUtil> mockedVariableElementUtil = mockStatic(VariableElementUtil.class)) {
            TypeElement givenElement = mock(TypeElement.class);
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            when(givenElement.getQualifiedName()).thenReturn(new NameImpl("TestClass"));

            TypeElement firstGivenSuperClass = mockErasuredSuperClass(
                    givenElement,
                    "FirstSuperClass",
                    mockedTypeMirrorUtil,
                    givenElementUtil,
                    givenTypeUtil
            );
            TypeElement secondGivenSuperClass = mockErasuredSuperClass(
                    firstGivenSuperClass,
                    "SecondSuperClass",
                    mockedTypeMirrorUtil,
                    givenElementUtil,
                    givenTypeUtil
            );
            mockErasuredSuperClass(
                    secondGivenSuperClass,
                    "java.lang.Object",
                    mockedTypeMirrorUtil,
                    givenElementUtil,
                    givenTypeUtil
            );

            List firstGivenEnclosedElements = singletonList(
                    createField(false, mockedVariableElementUtil, givenElementUtil, givenTypeUtil)
            );
            when(givenElement.getEnclosedElements()).thenReturn(firstGivenEnclosedElements);

            List secondGivenEnclosedElements = List.of(
                    createField(false, mockedVariableElementUtil, givenElementUtil, givenTypeUtil),
                    createField(false, mockedVariableElementUtil, givenElementUtil, givenTypeUtil)
            );
            when(firstGivenSuperClass.getEnclosedElements()).thenReturn(secondGivenEnclosedElements);

            List thirdGivenEnclosedElements = List.of(
                    createField(true, mockedVariableElementUtil, givenElementUtil, givenTypeUtil)
            );
            when(secondGivenSuperClass.getEnclosedElements()).thenReturn(thirdGivenEnclosedElements);

            assertTrue(isContainRepository(givenElement, givenElementUtil, givenTypeUtil));
        }
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void elementShouldNotContainRepository() {
        try (MockedStatic<TypeMirrorUtil> mockedTypeMirrorUtil = mockStatic(TypeMirrorUtil.class);
             MockedStatic<VariableElementUtil> mockedVariableElementUtil = mockStatic(VariableElementUtil.class)) {
            TypeElement givenElement = createTypeElement("TestClass");
            Elements givenElementUtil = mock(Elements.class);
            Types givenTypeUtil = mock(Types.class);

            TypeElement firstGivenSuperClass = mockErasuredSuperClass(
                    givenElement,
                    "FirstSuperClass",
                    mockedTypeMirrorUtil,
                    givenElementUtil,
                    givenTypeUtil
            );
            TypeElement secondGivenSuperClass = mockErasuredSuperClass(
                    firstGivenSuperClass,
                    "SecondSuperClass",
                    mockedTypeMirrorUtil,
                    givenElementUtil,
                    givenTypeUtil
            );
            mockErasuredSuperClass(
                    secondGivenSuperClass,
                    "java.lang.Object",
                    mockedTypeMirrorUtil,
                    givenElementUtil,
                    givenTypeUtil
            );

            List firstGivenEnclosedElements = singletonList(
                    createField(false, mockedVariableElementUtil, givenElementUtil, givenTypeUtil)
            );
            when(givenElement.getEnclosedElements()).thenReturn(firstGivenEnclosedElements);

            List secondGivenEnclosedElements = List.of(
                    createField(false, mockedVariableElementUtil, givenElementUtil, givenTypeUtil),
                    createField(false, mockedVariableElementUtil, givenElementUtil, givenTypeUtil)
            );
            when(firstGivenSuperClass.getEnclosedElements()).thenReturn(secondGivenEnclosedElements);

            List thirdGivenEnclosedElements = List.of(
                    createField(false, mockedVariableElementUtil, givenElementUtil, givenTypeUtil)
            );
            when(secondGivenSuperClass.getEnclosedElements()).thenReturn(thirdGivenEnclosedElements);

            assertFalse(isContainRepository(givenElement, givenElementUtil, givenTypeUtil));
        }
    }

    private TypeElement createTypeElement(String name) {
        TypeElement element = mock(TypeElement.class);
        when(element.getQualifiedName()).thenReturn(new NameImpl(name));
        return element;
    }

    private TypeElement mockErasuredSuperClass(TypeElement element,
                                               String name,
                                               MockedStatic<TypeMirrorUtil> mockedTypeMirrorUtil,
                                               Elements elementUtil,
                                               Types typeUtil) {
        TypeMirror superClassMirror = mock(TypeMirror.class);
        when(element.getSuperclass()).thenReturn(superClassMirror);
        TypeElement erasuredSuperClass = createTypeElement(name);
        mockedTypeMirrorUtil.when(
                () -> getErasuredTypeElement(
                        same(superClassMirror),
                        same(elementUtil),
                        same(typeUtil)
                )
        ).thenReturn(erasuredSuperClass);
        return erasuredSuperClass;
    }

    private VariableElement createField(boolean jpaRepositoryField,
                                        MockedStatic<VariableElementUtil> mockedVariableElementUtil,
                                        Elements elementUtil,
                                        Types typeUtil) {
        VariableElement element = mock(VariableElement.class);
        mockedVariableElementUtil.when(() -> isJpaRepositoryField(same(element), same(elementUtil), same(typeUtil)))
                .thenReturn(jpaRepositoryField);
        return element;
    }
}
