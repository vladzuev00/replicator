package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedRepository;
import by.aurorasoft.replicator.registry.ReplicatedRepositoryRegistry;
import by.aurorasoft.replicator.util.ProxyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toUnmodifiableSet;

@Component
@RequiredArgsConstructor
public final class ReplicatedRepositoryRegistryFactory {
    private final ApplicationContext context;

    public ReplicatedRepositoryRegistry create() {
        return context.getBeansWithAnnotation(ReplicatedRepository.class)
                .values()
                .stream()
//                .map(ProxyUtil::unProxy)
                .map(object -> (JpaRepository<?, ?>) object)
                .collect(collectingAndThen(toUnmodifiableSet(), ReplicatedRepositoryRegistry::new));
    }
}
