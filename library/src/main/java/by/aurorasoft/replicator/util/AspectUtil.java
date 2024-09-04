package by.aurorasoft.replicator.util;

import lombok.experimental.UtilityClass;
import org.aspectj.lang.JoinPoint;

@UtilityClass
public final class AspectUtil {

    public static Iterable<?> getIterableFirstArgument(JoinPoint joinPoint) {
        return (Iterable<?>) getFirstArgument(joinPoint);
    }

    public static Object getFirstArgument(JoinPoint joinPoint) {
        return joinPoint.getArgs()[0];
    }
}
