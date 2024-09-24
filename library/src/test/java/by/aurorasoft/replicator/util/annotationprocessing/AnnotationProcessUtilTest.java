package by.aurorasoft.replicator.util.annotationprocessing;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import by.aurorasoft.replicator.util.NameImpl;
import org.checkerframework.javacutil.TypesUtils;
import org.mockito.MockedStatic;
import org.testng.annotations.Test;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Stream;

import static by.aurorasoft.replicator.util.annotationprocessing.AnnotationProcessUtil.*;
import static by.aurorasoft.replicator.util.annotationprocessing.ExecutableElementUtil.ID_GETTER_NAME;
import static by.aurorasoft.replicator.util.annotationprocessing.TypeMirrorUtil.JPA_REPOSITORY_TYPE_NAME;
import static java.util.Collections.emptyList;
import static javax.lang.model.element.ElementKind.PACKAGE;
import static javax.lang.model.element.ElementKind.*;
import static javax.lang.model.element.Modifier.*;
import static javax.lang.model.type.TypeKind.*;
import static org.checkerframework.javacutil.TypesUtils.isErasedSubtype;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public final class AnnotationProcessUtilTest {

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void typeParameterElementShouldContainIdGetter() {
        TypeParameterElement givenElement = mock(TypeParameterElement.class);

        Element givenGenericElement = mock(Element.class);
        when(givenElement.getGenericElement()).thenReturn(givenGenericElement);

        Element firstGivenEnclosedElement = mock(Element.class);
        Element secondGivenEnclosedElement = createExecutableElement(
                METHOD,
                Set.of(SYNCHRONIZED, VOLATILE),
                ID_GETTER_NAME,
                emptyList()
        );
        Element thirdGivenEnclosedElement = createExecutableElement(
                METHOD,
                Set.of(SYNCHRONIZED, VOLATILE, PUBLIC),
                ID_GETTER_NAME,
                emptyList()
        );
        Element fourthGivenEnclosedElement = mock(Element.class);
        List givenEnclosedElements = List.of(
                firstGivenEnclosedElement,
                secondGivenEnclosedElement,
                thirdGivenEnclosedElement,
                fourthGivenEnclosedElement
        );
        when(givenGenericElement.getEnclosedElements()).thenReturn(givenEnclosedElements);

        assertTrue(isContainIdGetter(givenElement));
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void typeParameterElementShouldNotContainIdGetter() {
        TypeParameterElement givenElement = mock(TypeParameterElement.class);

        Element givenGenericElement = mock(Element.class);
        when(givenElement.getGenericElement()).thenReturn(givenGenericElement);

        Element firstGivenEnclosedElement = mock(Element.class);
        Element secondGivenEnclosedElement = createExecutableElement(
                METHOD,
                Set.of(SYNCHRONIZED, VOLATILE),
                ID_GETTER_NAME,
                emptyList()
        );
        Element thirdGivenEnclosedElement = createExecutableElement(
                METHOD,
                Set.of(SYNCHRONIZED, VOLATILE, PUBLIC),
                "GetId",
                emptyList()
        );
        Element fourthGivenEnclosedElement = mock(Element.class);
        List givenEnclosedElements = List.of(
                firstGivenEnclosedElement,
                secondGivenEnclosedElement,
                thirdGivenEnclosedElement,
                fourthGivenEnclosedElement
        );
        when(givenGenericElement.getEnclosedElements()).thenReturn(givenEnclosedElements);

        assertFalse(isContainIdGetter(givenElement));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private ExecutableElement createExecutableElement(ElementKind kind,
                                                      Set<Modifier> modifiers,
                                                      String name,
                                                      List parameters) {
        ExecutableElement element = mock(ExecutableElement.class);
        when(element.getKind()).thenReturn(kind);
        when(element.getModifiers()).thenReturn(modifiers);
        when(element.getSimpleName()).thenReturn(new NameImpl(name));
        when(element.getParameters()).thenReturn(parameters);
        return element;
    }
}
