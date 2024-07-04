package by.aurorasoft.replicator.holder.service;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import static java.util.Objects.requireNonNullElse;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;
import static org.springframework.aop.framework.AopProxyUtils.getSingletonTarget;

@Component
@RequiredArgsConstructor
public final class ReplicatedServiceRegistryFactory {
    private final ApplicationContext context;

    public ReplicatedServiceRegistry create() {
        return context.getBeansWithAnnotation(ReplicatedService.class)
                .values()
                .stream()
                .map(this::unProxy)
                .collect(collectingAndThen(toSet(), ReplicatedServiceRegistry::new));
    }

    private Object unProxy(final Object service) {
        return requireNonNullElse(getSingletonTarget(service), service);
    }
}
