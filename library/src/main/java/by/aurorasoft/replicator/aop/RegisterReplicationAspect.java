package by.aurorasoft.replicator.aop;

import by.aurorasoft.replicator.holder.ReplicationProducerHolder;
import by.aurorasoft.replicator.model.replication.produced.DeleteProducedReplication;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;

import java.util.List;

import static org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization;

@Aspect
@Component
@RequiredArgsConstructor
public class RegisterReplicationAspect {
    private final ReplicationProducerHolder producerHolder;

    @AfterReturning(pointcut = "replicatedCreate()", returning = "dto")
    public void registerCreate(final JoinPoint joinPoint, final Object dto) {
        registerSaveReplication(dto, joinPoint);
    }

    @AfterReturning(pointcut = "replicatedCreateAll()", returning = "dtos")
    public void registerCreateAll(final JoinPoint joinPoint, final List<?> dtos) {
        dtos.forEach(dto -> registerSaveReplication(dto, joinPoint));
    }

    @AfterReturning(pointcut = "replicatedUpdate()", returning = "dto")
    public void registerUpdate(final JoinPoint joinPoint, final Object dto) {
        registerSaveReplication(dto, joinPoint);
    }

    @Around("replicatedDeleteById()")
    public Object registerDeleteById(final ProceedingJoinPoint joinPoint)
            throws Throwable {
        final Object entityId = joinPoint.getArgs()[0];
        final Object result = joinPoint.proceed();
        registerDeleteReplication(entityId, joinPoint);
        return result;
    }

    private void registerSaveReplication(final Object dto, final JoinPoint joinPoint) {
        registerReplication(new SaveProducedReplication(dto), joinPoint);
    }

    private void registerDeleteReplication(final Object entityId, final JoinPoint joinPoint) {
        registerReplication(new DeleteProducedReplication(entityId), joinPoint);
    }

    private void registerReplication(final ProducedReplication replication, final JoinPoint joinPoint) {
        final ReplicationProducer producer = getProducer(joinPoint);
        final ReplicationCallback callback = new ReplicationCallback(producer, replication);
        registerSynchronization(callback);
    }

    private ReplicationProducer getProducer(final JoinPoint joinPoint) {
        return producerHolder.findByService(joinPoint.getTarget())
                .orElseThrow(() -> createNoProducerException(joinPoint));
    }

    private NoReplicationProducerException createNoProducerException(final JoinPoint joinPoint) {
        return new NoReplicationProducerException("There is no replication producer for '%s'".formatted(joinPoint));
    }

    @Pointcut("replicatedCRUDService() && create()")
    private void replicatedCreate() {

    }

    @Pointcut("replicatedCRUDService() && createAll()")
    private void replicatedCreateAll() {

    }

    @Pointcut("replicatedRUDService() && (update() || updatePartial())")
    private void replicatedUpdate() {

    }

    @Pointcut("replicatedRUDService() && deleteById()")
    private void replicatedDeleteById() {

    }

    @Pointcut("replicatedService() && rudService()")
    private void replicatedRUDService() {

    }

    @Pointcut("replicatedService() && crudService()")
    private void replicatedCRUDService() {

    }

    @Pointcut("@target(by.aurorasoft.replicator.annotation.ReplicatedService)")
    private void replicatedService() {

    }

    @Pointcut("rudServiceV1() || rudServiceV2()")
    private void rudService() {

    }

    @Pointcut("target(by.nhorushko.crudgeneric.service.RudGenericService)")
    private void rudServiceV1() {

    }

    @Pointcut("target(by.nhorushko.crudgeneric.v2.service.AbsServiceRUD)")
    private void rudServiceV2() {

    }

    @Pointcut("crudServiceV1() || crudServiceV2()")
    private void crudService() {

    }

    @Pointcut("target(by.nhorushko.crudgeneric.service.CrudGenericService)")
    private void crudServiceV1() {

    }

    @Pointcut("target(by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD)")
    private void crudServiceV2() {

    }

    @Pointcut("execution(public Object+ *.save(Object+))")
    private void create() {

    }

    @Pointcut("execution(public java.util.List *.saveAll(java.util.Collection))")
    private void createAll() {

    }

    @Pointcut("execution("
            + "public by.nhorushko.crudgeneric.v2.domain.AbstractDto "
            + "*.update(by.nhorushko.crudgeneric.v2.domain.AbstractDto)"
            + ")")
    private void update() {

    }

    @Pointcut("execution(public by.nhorushko.crudgeneric.v2.domain.AbstractDto *.updatePartial(Object, Object))")
    private void updatePartial() {

    }

    @Pointcut("execution(public void *.delete(Object))")
    private void deleteById() {

    }

    @RequiredArgsConstructor
    @Getter
    static final class ReplicationCallback implements TransactionSynchronization {
        private final ReplicationProducer producer;
        private final ProducedReplication replication;

        @Override
        public void afterCommit() {
            producer.send(replication);
        }
    }

    static final class NoReplicationProducerException extends RuntimeException {

        @SuppressWarnings("unused")
        public NoReplicationProducerException() {

        }

        public NoReplicationProducerException(final String description) {
            super(description);
        }

        @SuppressWarnings("unused")
        public NoReplicationProducerException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public NoReplicationProducerException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
