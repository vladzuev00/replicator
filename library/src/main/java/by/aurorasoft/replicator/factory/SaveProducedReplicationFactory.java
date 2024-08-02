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
    private final EntityJsonViewFactory entityJsonViewFactory;

    public SaveProducedReplication create(Object savedEntity, JoinPoint joinPoint) {
        EntityView[] entityViewConfigs = joinPoint.getTarget()
                .getClass()
                .getAnnotation(ReplicatedRepository.class)
                .entityViews();
        EntityJsonView<Object> entityJsonView = entityJsonViewFactory.create(savedEntity, entityViewConfigs);
        return new SaveProducedReplication(entityJsonView);
    }
}
