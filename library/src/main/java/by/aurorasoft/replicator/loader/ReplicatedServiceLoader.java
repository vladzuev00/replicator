package by.aurorasoft.replicator.loader;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public final class ReplicatedServiceLoader {
    private final ApplicationContext applicationContext;

    public Collection<Object> load() {
        return applicationContext.getBeansWithAnnotation(ReplicatedService.class).values();
    }
}
