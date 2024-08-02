package by.aurorasoft.replicator.util;

import lombok.experimental.UtilityClass;

import static java.util.Objects.requireNonNullElse;
import static org.springframework.aop.framework.AopProxyUtils.getSingletonTarget;

@UtilityClass
public final class ProxyUtil {

    public static Object unProxy(Object object) {
        return requireNonNullElse(getSingletonTarget(object), object);
    }
}
