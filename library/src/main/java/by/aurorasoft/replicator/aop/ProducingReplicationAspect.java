package by.aurorasoft.replicator.aop;

import by.aurorasoft.replicator.producer.DeleteReplicationProducer;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.producer.SaveReplicationProducer;
import by.aurorasoft.replicator.registry.replicationproducer.DeleteReplicationProducerRegistry;
import by.aurorasoft.replicator.registry.replicationproducer.SaveReplicationProducerRegistry;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;

import java.util.List;

import static by.aurorasoft.replicator.util.IdUtil.getId;
import static org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization;

@Aspect
@Component
@RequiredArgsConstructor
public class ProducingReplicationAspect {
    private final SaveReplicationProducerRegistry saveProducerRegistry;
    private final DeleteReplicationProducerRegistry deleteProducerRegistry;

    @AfterReturning(pointcut = "save() || saveAndFlush()", returning = "savedEntity")
    public void produceSave(JoinPoint joinPoint, Object savedEntity) {
        getSaveProducer(joinPoint).ifPresent(producer -> produceAfterCommit(producer, savedEntity));
    }

    @AfterReturning(pointcut = "saveAll() || saveAllAndFlush()", returning = "savedEntities")
    public void produceSaveAll(JoinPoint joinPoint, List<?> savedEntities) {
        getSaveProducer(joinPoint).ifPresent(producer -> savedEntities.forEach(savedEntity -> produceAfterCommit(producer, savedEntity)));
    }

    @AfterReturning("deleteById()")
    public void produceDeleteById(JoinPoint joinPoint) {
        getDeleteProducer(joinPoint).ifPresent(producer -> produceAfterCommit(producer, joinPoint.getArgs()[0]));
    }

    @AfterReturning("delete()")
    public void produceDelete(JoinPoint joinPoint) {
        getDeleteProducer(joinPoint).ifPresent(producer -> produceAfterCommit(producer, getId(joinPoint.getArgs()[0])));
    }

    @AfterReturning("deleteByIds() || deleteByIdsInBatch()")
    public void produceDeleteByIds(JoinPoint joinPoint) {
        getDeleteProducer(joinPoint).ifPresent(producer -> getIterableFirstArgument(joinPoint).forEach(entityId -> produceAfterCommit(producer, entityId)));
    }

    @AfterReturning("deleteIterable() || deleteIterableInBatch()")
    public void produceDeleteIterable(JoinPoint joinPoint) {
        getDeleteProducer(joinPoint).ifPresent(producer -> getIterableFirstArgument(joinPoint).forEach(entity -> produceAfterCommit(producer, getId(entity))));
    }

    @AfterReturning("deleteAll() || deleteAllInBatch()")
    public void produceDeleteAll(JoinPoint joinPoint) {
        getDeleteProducer(joinPoint).ifPresent(producer -> findAllEntities(joinPoint).forEach(entity -> produceAfterCommit(producer, getId(entity))));
    }

    private Optional<SaveReplicationProducer> getSaveProducer(JoinPoint joinPoint) {
        return saveProducerRegistry.get(getRepository(joinPoint));
    }

    private Optional<DeleteReplicationProducer> getDeleteProducer(JoinPoint joinPoint) {
        return deleteProducerRegistry.get(getRepository(joinPoint));
    }

    private JpaRepository<?, ?> getRepository(JoinPoint joinPoint) {
        return (JpaRepository<?, ?>) joinPoint.getThis();
    }

    private void produceAfterCommit(ReplicationProducer<?> producer, Object model) {
        registerSynchronization(new ReplicationCallback(producer, model));
    }

    private Iterable<?> getIterableFirstArgument(JoinPoint joinPoint) {
        return (Iterable<?>) joinPoint.getArgs()[0];
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

    @RequiredArgsConstructor
    @Getter
    static final class ReplicationCallback implements TransactionSynchronization {
        private final ReplicationProducer<?> producer;
        private final Object model;

        @Override
        public void afterCommit() {
            producer.send(model);
        }
    }
}
