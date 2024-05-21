package by.aurorasoft.replicator.aop;

import by.aurorasoft.replicator.holder.ReplicationProducerHolder;
import by.aurorasoft.replicator.model.replication.produced.DeleteProducedReplication;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;

import java.util.List;

import static by.aurorasoft.replicator.util.IdUtil.getIds;
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

    @AfterReturning("replicatedDeleteById()")
    public void registerDeleteById(final JoinPoint joinPoint) {
        final Object id = joinPoint.getArgs()[0];
        registerDeleteReplication(id, joinPoint);
    }

    @SuppressWarnings("unchecked")
    @AfterReturning("replicatedDeleteAll()")
    public void registerDeleteAll(final JoinPoint joinPoint) {
        final List<Object> dtos = (List<Object>) joinPoint.getArgs()[0];
        getIds(dtos).forEach(id -> registerDeleteReplication(id, joinPoint));
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

    @Pointcut("replicatedService() && (crudServiceV1() || crudServiceV2()) && create()")
    private void replicatedCreate() {

    }

    @Pointcut("execution(public Object+ *.save(Object+))")
    private void create() {

    }

    @Pointcut("replicatedService() && (crudServiceV1() || crudServiceV2()) && createAll()")
    private void replicatedCreateAll() {

    }

    @Pointcut("execution(public java.util.List *.saveAll(java.util.Collection+))")
    private void createAll() {

    }

    @Pointcut("replicatedService() && (rudServiceV1() || rudServiceV2()) && (update() || updatePartial())")
    private void replicatedUpdate() {

    }

    @Pointcut("execution(public Object+ *.update(Object+))")
    private void update() {

    }

    @Pointcut("execution(public Object+ *.updatePartial(Object+, Object))")
    private void updatePartial() {

    }

    @Pointcut("replicatedService() && ((rudServiceV1() && deleteByIdV1()) || (rudServiceV2() && deleteByIdV2()))")
    private void replicatedDeleteById() {

    }

    @Pointcut("execution(public void *.deleteById(Long))")
    private void deleteByIdV1() {

    }

    @Pointcut("execution(public void *.delete(Object+))")
    private void deleteByIdV2() {

    }

    @Pointcut("replicatedService() && rudServiceV1() && deleteAll()")
    private void replicatedDeleteAll() {

    }

    @Pointcut("execution(public void *.deleteAll(java.util.List+))")
    private void deleteAll() {

    }

    @Pointcut("@target(by.aurorasoft.replicator.annotation.ReplicatedService)")
    private void replicatedService() {

    }

    @Pointcut("target(by.nhorushko.crudgeneric.service.RudGenericService)")
    private void rudServiceV1() {

    }

    @Pointcut("target(by.nhorushko.crudgeneric.v2.service.AbsServiceRUD)")
    private void rudServiceV2() {

    }

    @Pointcut("target(by.nhorushko.crudgeneric.service.CrudGenericService)")
    private void crudServiceV1() {

    }

    @Pointcut("target(by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD)")
    private void crudServiceV2() {

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
