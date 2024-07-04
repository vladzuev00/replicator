package by.aurorasoft.replicator.aop;

import by.aurorasoft.replicator.holder.producer.ReplicationProducerRegistry;
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

import static by.aurorasoft.replicator.util.IdUtil.getId;
import static org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization;

@Aspect
@Component
@RequiredArgsConstructor
public class ProducingReplicationAspect {
    private final ReplicationProducerRegistry producerRegistry;

    @AfterReturning(pointcut = "replicatedCreate()", returning = "dto")
    public void produceCreate(JoinPoint joinPoint, Object dto) {
        produceSaveReplication(dto, joinPoint);
    }

    @AfterReturning(pointcut = "replicatedCreateAll()", returning = "dtos")
    public void produceCreateAll(JoinPoint joinPoint, List<?> dtos) {
        dtos.forEach(dto -> produceSaveReplication(dto, joinPoint));
    }

    @AfterReturning(pointcut = "replicatedUpdate()", returning = "dto")
    public void produceUpdate(JoinPoint joinPoint, Object dto) {
        produceSaveReplication(dto, joinPoint);
    }

    @AfterReturning("replicatedDeleteById()")
    public void produceDeleteById(JoinPoint joinPoint) {
        Object id = joinPoint.getArgs()[0];
        produceDeleteReplication(id, joinPoint);
    }

    @SuppressWarnings("unchecked")
    @AfterReturning("replicatedDeleteAll()")
    public void produceDeleteAll(JoinPoint joinPoint) {
        List<Object> dtos = ((List<Object>) joinPoint.getArgs()[0]);
        dtos.forEach(dto -> produceDeleteReplication(getId(dto), joinPoint));
    }

    private void produceSaveReplication(Object dto, JoinPoint joinPoint) {
        produceReplication(new SaveProducedReplication(dto), joinPoint);
    }

    private void produceDeleteReplication(Object entityId, JoinPoint joinPoint) {
        produceReplication(new DeleteProducedReplication(entityId), joinPoint);
    }

    private void produceReplication(ProducedReplication replication, JoinPoint joinPoint) {
        ReplicationProducer producer = getProducer(joinPoint);
        ReplicationCallback callback = new ReplicationCallback(producer, replication);
        registerSynchronization(callback);
    }

    private ReplicationProducer getProducer(JoinPoint joinPoint) {
        return producerRegistry.get(joinPoint.getTarget()).orElseThrow(() -> createNoProducerException(joinPoint));
    }

    private IllegalStateException createNoProducerException(JoinPoint joinPoint) {
        return new IllegalStateException("There is no replication producer for '%s'".formatted(joinPoint));
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
}
