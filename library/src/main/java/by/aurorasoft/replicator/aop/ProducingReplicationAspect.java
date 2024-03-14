package by.aurorasoft.replicator.aop;

import by.aurorasoft.replicator.holder.ReplicationProducerHolder;
import by.aurorasoft.replicator.model.produced.DeleteProducedReplication;
import by.aurorasoft.replicator.model.produced.ProducedReplication;
import by.aurorasoft.replicator.model.produced.SaveProducedReplication;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static java.util.UUID.randomUUID;

@Aspect
@Component
@RequiredArgsConstructor
public class ProducingReplicationAspect {
    private final ReplicationProducerHolder producerHolder;

    @SuppressWarnings("rawtypes")
    @AfterReturning(pointcut = "replicatedCreate()", returning = "createdDto")
    public void replicateCreate(final JoinPoint joinPoint, final AbstractDto createdDto) {
        produceSaveReplication(createdDto, joinPoint);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @AfterReturning(pointcut = "replicatedCreateAll()", returning = "createdDtos")
    public void replicateCreateAll(final JoinPoint joinPoint, final List createdDtos) {
        ((List<AbstractDto>) createdDtos).forEach(dto -> produceSaveReplication(dto, joinPoint));
    }

    @SuppressWarnings("rawtypes")
    @AfterReturning(pointcut = "replicatedUpdate()", returning = "updatedDto")
    public void replicateUpdate(final JoinPoint joinPoint, final AbstractDto updatedDto) {
        produceSaveReplication(updatedDto, joinPoint);
    }

    @Around("replicatedDeleteById()")
    public Object replicateDeleteById(final ProceedingJoinPoint joinPoint)
            throws Throwable {
        final Object entityId = joinPoint.getArgs()[0];
        final Object result = joinPoint.proceed();
        produceDeleteReplication(entityId, joinPoint);
        return result;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void produceSaveReplication(final AbstractDto dto, final JoinPoint joinPoint) {
        produceReplication(uuid -> new SaveProducedReplication(uuid, dto), joinPoint);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void produceDeleteReplication(final Object entityId, final JoinPoint joinPoint) {
        produceReplication(uuid -> new DeleteProducedReplication(uuid, entityId), joinPoint);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void produceReplication(final Function<UUID, ProducedReplication> factory, final JoinPoint joinPoint) {
        final ProducedReplication replication = factory.apply(randomUUID());
        final AbsServiceRUD<?, ?, ?, ?, ?> service = (AbsServiceRUD<?, ?, ?, ?, ?>) joinPoint.getTarget();
        producerHolder.findByService(service)
                .orElseThrow(() -> createNoProducerException(service))
                .send(replication);
    }

    private NoReplicationProducerException createNoProducerException(final AbsServiceRUD<?, ?, ?, ?, ?> service) {
        throw new NoReplicationProducerException(
                "There is no replication producer for service %s".formatted(service.getClass())
        );
    }

    @Pointcut("replicatedCrudService() && create()")
    private void replicatedCreate() {

    }

    @Pointcut("replicatedCrudService() && createAll()")
    private void replicatedCreateAll() {

    }

    @Pointcut("replicatedRudService() && (update() || updatePartial())")
    private void replicatedUpdate() {

    }

    @Pointcut("replicatedRudService() && deleteById()")
    private void replicatedDeleteById() {

    }

    @Pointcut("replicatedService() && rudService()")
    private void replicatedRudService() {

    }

    @Pointcut("replicatedService() && crudService()")
    private void replicatedCrudService() {

    }

    @Pointcut("@target(by.aurorasoft.replicator.annotation.ReplicatedService)")
    private void replicatedService() {

    }

    @Pointcut("target(by.nhorushko.crudgeneric.v2.service.AbsServiceRUD)")
    private void rudService() {

    }

    @Pointcut("target(by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD)")
    private void crudService() {

    }

    @Pointcut("execution(public by.nhorushko.crudgeneric.v2.domain.AbstractDto *.save(by.nhorushko.crudgeneric.v2.domain.AbstractDto))")
    private void create() {

    }

    @Pointcut("execution(public java.util.List *.saveAll(java.util.Collection))")
    private void createAll() {

    }

    @Pointcut("execution(public by.nhorushko.crudgeneric.v2.domain.AbstractDto *.update(by.nhorushko.crudgeneric.v2.domain.AbstractDto))")
    private void update() {

    }

    @Pointcut("execution(public by.nhorushko.crudgeneric.v2.domain.AbstractDto *.updatePartial(Object, Object))")
    private void updatePartial() {

    }

    @Pointcut("execution(public void *.delete(Object))")
    private void deleteById() {

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
