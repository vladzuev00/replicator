package by.aurorasoft.replicator.aop;

import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static by.aurorasoft.replicator.util.IdUtil.getId;

@Aspect
@Component
@RequiredArgsConstructor
public class ProducingReplicationAspect {
    private final ReplicationProducerRegistry producerRegistry;

    @AfterReturning(pointcut = "save() || saveAndFlush()", returning = "savedEntity")
    public void produceSave(JoinPoint joinPoint, Object savedEntity) {
        getProducer(joinPoint).ifPresent(producer -> producer.produceSaveAfterCommit(savedEntity));
    }

    @AfterReturning(pointcut = "saveAll() || saveAllAndFlush()", returning = "savedEntities")
    public void produceSaveAll(JoinPoint joinPoint, List<?> savedEntities) {
        getProducer(joinPoint).ifPresent(producer -> savedEntities.forEach(producer::produceSaveAfterCommit));
    }

    @AfterReturning("deleteById()")
    public void produceDeleteById(JoinPoint joinPoint) {
        getProducer(joinPoint).ifPresent(producer -> producer.produceDeleteAfterCommit(getFirstArgument(joinPoint)));
    }

    @AfterReturning("delete()")
    public void produceDelete(JoinPoint joinPoint) {
        getProducer(joinPoint)
                .ifPresent(producer -> producer.produceDeleteAfterCommit(getId(getFirstArgument(joinPoint))));
    }

    @AfterReturning("deleteByIds() || deleteByIdsInBatch()")
    public void produceDeleteByIds(JoinPoint joinPoint) {
        getProducer(joinPoint)
                .ifPresent(producer -> getIterableFirstArgument(joinPoint).forEach(producer::produceDeleteAfterCommit));
    }

    @AfterReturning("deleteIterable() || deleteIterableInBatch()")
    public void produceDeleteIterable(JoinPoint joinPoint) {
        getProducer(joinPoint)
                .ifPresent(
                        producer -> getIterableFirstArgument(joinPoint)
                                .forEach(entity -> producer.produceDeleteAfterCommit(getId(entity)))
                );
    }

    @AfterReturning("deleteAll() || deleteAllInBatch()")
    public void produceDeleteAll(JoinPoint joinPoint) {
        getProducer(joinPoint)
                .ifPresent(
                        producer -> findAllEntities(joinPoint)
                                .forEach(entity -> producer.produceDeleteAfterCommit(getId(entity)))
                );
    }

    private Optional<ReplicationProducer> getProducer(JoinPoint joinPoint) {
        return producerRegistry.get(getRepository(joinPoint));
    }

    private JpaRepository<?, ?> getRepository(JoinPoint joinPoint) {
        return (JpaRepository<?, ?>) joinPoint.getThis();
    }

    private Iterable<?> getIterableFirstArgument(JoinPoint joinPoint) {
        return (Iterable<?>) joinPoint.getArgs()[0];
    }

    private Object getFirstArgument(JoinPoint joinPoint) {
        return joinPoint.getArgs()[0];
    }

    private List<?> findAllEntities(JoinPoint joinPoint) {
        return getRepository(joinPoint).findAll();
    }

    @Pointcut("execution(public Object+ org.springframework.data.jpa.repository.JpaRepository+.save(Object+))")
    private void save() {

    }

    @Pointcut("execution(public Object+ org.springframework.data.jpa.repository.JpaRepository+.saveAndFlush(Object+))")
    private void saveAndFlush() {

    }

    @Pointcut("execution(public java.util.List+ org.springframework.data.jpa.repository.JpaRepository+.saveAll(Iterable+))")
    private void saveAll() {

    }

    @Pointcut("execution(public java.util.List+ org.springframework.data.jpa.repository.JpaRepository+.saveAllAndFlush(Iterable+))")
    private void saveAllAndFlush() {

    }

    @Pointcut("execution(public void org.springframework.data.jpa.repository.JpaRepository+.deleteById(Object+))")
    private void deleteById() {

    }

    @Pointcut("execution(public void org.springframework.data.jpa.repository.JpaRepository+.delete(Object+))")
    private void delete() {

    }

    @Pointcut("execution(public void org.springframework.data.jpa.repository.JpaRepository+.deleteAllById(Iterable+))")
    private void deleteByIds() {

    }

    @Pointcut("execution(public void org.springframework.data.jpa.repository.JpaRepository+.deleteAllByIdInBatch(Iterable+))")
    private void deleteByIdsInBatch() {

    }

    @Pointcut("execution(public void org.springframework.data.jpa.repository.JpaRepository+.deleteAll(Iterable+))")
    private void deleteIterable() {

    }

    @Pointcut("execution(public void org.springframework.data.jpa.repository.JpaRepository+.deleteAllInBatch(Iterable+))")
    private void deleteIterableInBatch() {

    }

    @Pointcut("execution(public void org.springframework.data.jpa.repository.JpaRepository+.deleteAll())")
    private void deleteAll() {

    }

    @Pointcut(
            "execution(public void org.springframework.data.jpa.repository.JpaRepository+.deleteAllInBatch())"
                    + " || "
                    + "execution(public void org.springframework.data.jpa.repository.JpaRepository+.deleteInBatch())"
    )
    private void deleteAllInBatch() {

    }
}
