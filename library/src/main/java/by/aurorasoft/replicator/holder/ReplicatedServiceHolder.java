package by.aurorasoft.replicator.holder;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Objects.requireNonNullElse;
import static org.springframework.aop.framework.AopProxyUtils.getSingletonTarget;
import static org.springframework.aop.framework.AopProxyUtils.ultimateTargetClass;

@Component
@Getter
public final class ReplicatedServiceHolder {
    private final List<? extends AbsServiceRUD<?, ?, ?, ?, ?>> services;

    public ReplicatedServiceHolder(final List<AbsServiceRUD<?, ?, ?, ?, ?>> services) {
        this.services = findReplicatedUnProxying(services);
    }

    private static List<? extends AbsServiceRUD<?, ?, ?, ?, ?>> findReplicatedUnProxying(
            final List<AbsServiceRUD<?, ?, ?, ?, ?>> services
    ) {
        return services.stream()
                .filter(ReplicatedServiceHolder::isReplicated)
                .map(ReplicatedServiceHolder::unProxy)
                .toList();
    }

    private static boolean isReplicated(final AbsServiceRUD<?, ?, ?, ?, ?> service) {
        return ultimateTargetClass(service).isAnnotationPresent(ReplicatedService.class);
    }

    private static AbsServiceRUD<?, ?, ?, ?, ?> unProxy(final AbsServiceRUD<?, ?, ?, ?, ?> service) {
        return (AbsServiceRUD<?, ?, ?, ?, ?>) requireNonNullElse(getSingletonTarget(service), service);
    }
}
