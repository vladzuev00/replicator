package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedRepository;
import by.aurorasoft.replicator.registry.ReplicatedRepositoryRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import static java.util.Objects.requireNonNullElse;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toUnmodifiableSet;
import static org.springframework.aop.framework.AopProxyUtils.getSingletonTarget;

@Component
@RequiredArgsConstructor
public final class ReplicatedRepositoryRegistryFactory {
    private final ApplicationContext context;

    public ReplicatedRepositoryRegistry create() {
        return null;
//        return context.getBeansWithAnnotation(ReplicatedRepository.class)
//                .values()
//                .stream()
//                .map(this::unProxy)
//                .collect(collectingAndThen(toUnmodifiableSet(), ReplicatedRepositoryRegistry::new));
    }

    private Object unProxy(Object service) {
        return requireNonNullElse(getSingletonTarget(service), service);
    }
}
