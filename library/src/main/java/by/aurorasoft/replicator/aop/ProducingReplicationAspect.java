package by.aurorasoft.replicator.aop;

import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
import by.aurorasoft.replicator.util.PropertyUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

import static by.aurorasoft.replicator.util.PropertyUtil.getId;
import static by.aurorasoft.replicator.util.PropertyUtil.getJpaRepository;

@Aspect
@Component
@RequiredArgsConstructor
public class ProducingReplicationAspect {
    private final ReplicationProducerRegistry producerRegistry;

    @AfterReturning(
            pointcut = "@annotation(by.aurorasoft.replicator.annotation.operation.ReplicatedSave)",
            returning = "savedDto"
    )
    public void produceSave(JoinPoint joinPoint, Object savedDto) {
        getProducer(joinPoint).produceSaveAfterCommit(savedDto);
    }

    @AfterReturning(
            pointcut = "@annotation(by.aurorasoft.replicator.annotation.operation.ReplicatedSaveAll)",
            returning = "savedDtos"
    )
    public void produceSaveAll(JoinPoint joinPoint, List<?> savedDtos) {
        ReplicationProducer producer = getProducer(joinPoint);
        savedDtos.forEach(producer::produceSaveAfterCommit);
    }

    @AfterReturning("@annotation(by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteById)")
    public void produceDeleteById(JoinPoint joinPoint) {
        getProducer(joinPoint).produceDeleteAfterCommit(getFirstArgument(joinPoint));
    }

    @AfterReturning("@annotation(by.aurorasoft.replicator.annotation.operation.ReplicatedDelete)")
    public void produceDelete(JoinPoint joinPoint) {
        getProducer(joinPoint).produceDeleteAfterCommit(getId(getFirstArgument(joinPoint)));
    }

    @AfterReturning("@annotation(by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteByIds)")
    public void produceDeleteByIds(JoinPoint joinPoint) {
        ReplicationProducer producer = getProducer(joinPoint);
        getIterableFirstArgument(joinPoint).forEach(producer::produceDeleteAfterCommit);
    }

    @AfterReturning("@annotation(by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteIterable)")
    public void produceDeleteIterable(JoinPoint joinPoint) {
        ReplicationProducer producer = getProducer(joinPoint);
        getIterableFirstArgument(joinPoint).forEach(dto -> producer.produceDeleteAfterCommit(getId(dto)));
    }

    @AfterReturning("@annotation(by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteAll)")
    public void produceDeleteAll(JoinPoint joinPoint) {
        ReplicationProducer producer = getProducer(joinPoint);
        getRepository(joinPoint)
                .findAll()
                .stream()
                .map(PropertyUtil::getId)
                .forEach(producer::produceDeleteAfterCommit);
    }

    private ReplicationProducer getProducer(JoinPoint joinPoint) {
        Object service = getService(joinPoint);
        return producerRegistry.get(service).orElseThrow(() -> createNoProducerException(service));
    }

    private Object getService(JoinPoint joinPoint) {
        return joinPoint.getThis();
    }

    private IllegalStateException createNoProducerException(Object service) {
        return new IllegalStateException("There is no producer for %s".formatted(service.getClass().getName()));
    }

    private Iterable<?> getIterableFirstArgument(JoinPoint joinPoint) {
        return (Iterable<?>) joinPoint.getArgs()[0];
    }

    private Object getFirstArgument(JoinPoint joinPoint) {
        return joinPoint.getArgs()[0];
    }

    private JpaRepository<?, ?> getRepository(JoinPoint joinPoint) {
        return getJpaRepository(getService(joinPoint));
    }
}
