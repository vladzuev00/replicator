package by.aurorasoft.replicator.util;

import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.Test;

import static by.aurorasoft.replicator.util.AspectUtil.getFirstArgument;
import static by.aurorasoft.replicator.util.AspectUtil.getIterableFirstArgument;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class AspectUtilTest {

    @Test
    public void iterableFirstArgumentShouldBeGot() {
        JoinPoint givenJoinPoint = mock(JoinPoint.class);

        Iterable<?> givenFirstArgument = mock(Iterable.class);
        Object[] givenArguments = {givenFirstArgument, new Object(), new Object()};
        when(givenJoinPoint.getArgs()).thenReturn(givenArguments);

        Iterable<?> actual = getIterableFirstArgument(givenJoinPoint);
        assertSame(givenFirstArgument, actual);
    }

    @Test
    public void firstArgumentShouldBeGot() {
        JoinPoint givenJoinPoint = mock(JoinPoint.class);

        Long givenFirstArgument = 255L;
        Object[] givenArguments = {givenFirstArgument, new Object(), new Object()};
        when(givenJoinPoint.getArgs()).thenReturn(givenArguments);

        Object actual = getFirstArgument(givenJoinPoint);
        assertSame(givenFirstArgument, actual);
    }
}
