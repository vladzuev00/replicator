package by.aurorasoft.replicator.aop;

import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
import by.aurorasoft.replicator.util.PropertyUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.List;

import static by.aurorasoft.replicator.util.AspectUtil.getFirstArgument;
import static by.aurorasoft.replicator.util.AspectUtil.getIterableFirstArgument;
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

    @Before("@annotation(by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteAll)")
    public void produceDeleteAll(JoinPoint joinPoint) {
        ReplicationProducer producer = getProducer(joinPoint);
        getJpaRepository(joinPoint.getTarget())
                .findAll()
                .stream()
                .map(PropertyUtil::getId)
                .forEach(producer::produceDeleteAfterCommit);
    }

    private ReplicationProducer getProducer(JoinPoint joinPoint) {
        Object service = joinPoint.getThis();
        return producerRegistry.get(service).orElseThrow(() -> createNoProducerException(service));
    }

    private IllegalStateException createNoProducerException(Object service) {
        return new IllegalStateException("There is no producer for %s".formatted(service.getClass().getName()));
    }
}
