package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.annotation.ReplicatedService.DtoViewConfig;
import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
import by.aurorasoft.replicator.model.view.DtoJsonView;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class SaveProducedReplicationFactory {
    private final DtoJsonViewFactory dtoJsonViewFactory;

    public SaveProducedReplication create(Object dto, JoinPoint joinPoint) {
        DtoViewConfig[] dtoViewConfigs = joinPoint.getTarget()
                .getClass()
                .getAnnotation(ReplicatedService.class)
                .dtoViewConfigs();
        DtoJsonView<Object> dtoJsonView = dtoJsonViewFactory.create(dto, dtoViewConfigs);
        return new SaveProducedReplication(dtoJsonView);
    }
}
