package by.aurorasoft.replicator.util;

import lombok.experimental.UtilityClass;

import java.lang.annotation.Annotation;

import static java.util.Objects.requireNonNull;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

@UtilityClass
public final class AnnotationUtil {

    public static <A extends Annotation> A getAnnotation(Class<?> type, Class<A> annotationType) {
        return requireNonNull(findAnnotation(type, annotationType));
    }
}
