package by.aurorasoft.replicator.aop;

import by.aurorasoft.replicator.factory.SaveProducedReplicationFactory;
import by.aurorasoft.replicator.model.replication.produced.DeleteProducedReplication;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
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
    private final ReplicationProducerRegistry producerRegistry;
    private final SaveProducedReplicationFactory saveReplicationFactory;

    @AfterReturning(
            pointcut = "replicatedRepository() && (save() || saveAndFlush())",
            returning = "savedEntity"
    )
    public void produceSave(JoinPoint joinPoint, Object savedEntity) {
        produceSaveReplication(savedEntity, joinPoint);
    }

    @AfterReturning(
            pointcut = "replicatedRepository() && (saveAll() || saveAllAndFlush())",
            returning = "savedEntities"
    )
    public void produceSaveAll(JoinPoint joinPoint, List<Object> savedEntities) {
        savedEntities.forEach(entity -> produceSaveReplication(entity, joinPoint));
    }

    @AfterReturning("replicatedRepository() && deleteById()")
    public void produceDeleteById(JoinPoint joinPoint) {
        Object entityId = joinPoint.getArgs()[0];
        produceDeleteReplication(entityId, joinPoint);
    }

    @AfterReturning("replicatedRepository() && delete()")
    public void produceDelete(JoinPoint joinPoint) {
        Object entity = joinPoint.getArgs()[0];
        produceDeleteReplication(getId(entity), joinPoint);
    }

    @AfterReturning("replicatedRepository() && (deleteByIds() || deleteByIdsInBatch())")
    public void produceDeleteByIds(JoinPoint joinPoint) {
        getFirstArgumentAsIterable(joinPoint).forEach(id -> produceDeleteReplication(id, joinPoint));
    }

    @AfterReturning("replicatedRepository() && (deleteIterable() || deleteIterableInBatch())")
    public void produceDeleteIterable(JoinPoint joinPoint) {
        getFirstArgumentAsIterable(joinPoint).forEach(entity -> produceDeleteReplication(getId(entity), joinPoint));
    }

    @AfterReturning("replicatedRepository() && (deleteAll() || deleteAllInBatch())")
    public void produceDeleteAll(JoinPoint joinPoint) {
        findAllEntities(joinPoint).forEach(entity -> produceDeleteReplication(getId(entity), joinPoint));
    }

    private void produceSaveReplication(Object savedEntity, JoinPoint joinPoint) {
        SaveProducedReplication replication = saveReplicationFactory.create(savedEntity, joinPoint);
        produceReplication(replication, joinPoint);
    }

    private void produceDeleteReplication(Object entityId, JoinPoint joinPoint) {
        DeleteProducedReplication replication = new DeleteProducedReplication(entityId);
        produceReplication(replication, joinPoint);
    }

    private void produceReplication(ProducedReplication<?> replication, JoinPoint joinPoint) {
        JpaRepository<?, ?> repository = (JpaRepository<?, ?>) joinPoint.getTarget();
        ReplicationProducer producer = getProducer(repository);
        ReplicationCallback callback = new ReplicationCallback(producer, replication);
        registerSynchronization(callback);
    }

    private ReplicationProducer getProducer(JpaRepository<?, ?> repository) {
        return producerRegistry.get(repository).orElseThrow(() -> createNoProducerException(repository));
    }

    private IllegalStateException createNoProducerException(JpaRepository<?, ?> repository) {
        return new IllegalStateException("There is no producer for %s".formatted(repository.getClass().getName()));
    }

    @SuppressWarnings("unchecked")
    private Iterable<Object> getFirstArgumentAsIterable(JoinPoint joinPoint) {
        return (Iterable<Object>) joinPoint.getArgs()[0];
    }

    private List<?> findAllEntities(JoinPoint joinPoint) {
        JpaRepository<?, ?> repository = (JpaRepository<?, ?>) joinPoint.getTarget();
        return repository.findAll();
    }

    @Pointcut("@target(by.aurorasoft.replicator.annotation.ReplicatedRepository)")
    private void replicatedRepository() {

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
        private final ReplicationProducer producer;
        private final ProducedReplication<?> replication;

        @Override
        public void afterCommit() {
            producer.send(replication);
        }
    }
}
