package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.annotation.ReplicatedService.ViewConfig;
import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
import by.aurorasoft.replicator.model.view.DtoJsonView;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class SaveProducedReplicationFactory {
    private final DtoJsonViewFactory dtoJsonViewFactory;

    public SaveProducedReplication create(Object savedDto, JoinPoint joinPoint) {
        ViewConfig[] viewConfigs = joinPoint.getTarget()
                .getClass()
                .getAnnotation(ReplicatedService.class)
                .viewConfigs();
        DtoJsonView<Object> dtoJsonView = dtoJsonViewFactory.create(savedDto, viewConfigs);
        return new SaveProducedReplication(dtoJsonView);
    }
}
