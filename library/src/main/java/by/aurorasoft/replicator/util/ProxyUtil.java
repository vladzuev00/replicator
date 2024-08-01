package by.aurorasoft.replicator.util;

import lombok.experimental.UtilityClass;

import static java.util.Objects.requireNonNullElse;
import static org.springframework.aop.framework.AopProxyUtils.getSingletonTarget;

//TODO: test and use everywhere
@UtilityClass
public final class ProxyUtil {

    public static Object unProxy(Object object) {
        return requireNonNullElse(getSingletonTarget(object), object);
    }
}
