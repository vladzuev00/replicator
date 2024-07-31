package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedRepository;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.EntityView;
import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
import by.aurorasoft.replicator.model.view.EntityJsonView;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class SaveProducedReplicationFactory {
    private final DtoJsonViewFactory dtoJsonViewFactory;

    public SaveProducedReplication create(Object savedDto, JoinPoint joinPoint) {
        EntityView[] viewConfigs = joinPoint.getTarget()
                .getClass()
                .getAnnotation(ReplicatedRepository.class)
                .entityViews();
        EntityJsonView<Object> dtoJsonView = dtoJsonViewFactory.create(savedDto, viewConfigs);
        return new SaveProducedReplication(dtoJsonView);
    }
}
