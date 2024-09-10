package by.aurorasoft.replicator.util;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import static by.aurorasoft.replicator.testutil.AssertExceptionUtil.executeExpectingException;
import static by.aurorasoft.replicator.util.PropertyUtil.getId;
import static by.aurorasoft.replicator.util.PropertyUtil.getJpaRepository;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

public final class PropertyUtilTest {

    @Test
    public void idShouldBeGot() {
        Object givenId = new Object();
        FirstTestDto givenObject = new FirstTestDto(givenId);

        Object actual = getId(givenObject);
        assertSame(givenId, actual);
    }

    @Test
    public void idShouldNotBeGotBecauseOfNoGetter() {
        Object givenId = new Object();
        SecondTestDto givenObject = new SecondTestDto(givenId);

        executeExpectingException(
                () -> getId(givenObject),
                NullPointerException.class,
                "There is no id's getter in class by.aurorasoft.replicator.util.PropertyUtilTest$SecondTestDto"
        );
    }

    @Test
    public void repositoryShouldBeGot() {
        JpaRepository<?, ?> givenRepository = mock(JpaRepository.class);
        FirstTestService givenService = new FirstTestService(givenRepository);

        JpaRepository<?, ?> actual = getJpaRepository(givenService);
        assertSame(givenRepository, actual);
    }

    @Test
    public void repositoryShouldNotBeGot() {
        JpaRepository<?, ?> givenRepository = mock(JpaRepository.class);
        SecondTestService givenService = new SecondTestService(givenRepository);

        executeExpectingException(
                () -> getJpaRepository(givenService),
                NullPointerException.class,
                "There is no field 'repository' in 'class by.aurorasoft.replicator.util.PropertyUtilTest$SecondTestService'"
        );
    }

    @Value
    private static class FirstTestDto {
        Object id;
    }

    @RequiredArgsConstructor
    private static final class SecondTestDto {

        @SuppressWarnings("unused")
        private final Object id;
    }

    @RequiredArgsConstructor
    private static final class FirstTestService {

        @SuppressWarnings("unused")
        private final JpaRepository<?, ?> repository;
    }

    @RequiredArgsConstructor
    private static final class SecondTestService {

        @SuppressWarnings("unused")
        private final JpaRepository<?, ?> jpaRepository;
    }
}
