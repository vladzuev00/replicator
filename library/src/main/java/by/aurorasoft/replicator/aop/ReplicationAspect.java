package by.aurorasoft.replicator.aop;

import by.aurorasoft.replicator.holder.ReplicationProducerHolder;
import by.aurorasoft.replicator.model.DeleteReplication;
import by.aurorasoft.replicator.model.Replication;
import by.aurorasoft.replicator.model.SaveReplication;
import by.aurorasoft.replicator.model.UpdateReplication;
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

@Aspect
@Component
@RequiredArgsConstructor
public class ReplicationAspect {
    private final ReplicationProducerHolder producerHolder;

    @SuppressWarnings({"rawtypes", "unchecked"})
    @AfterReturning(pointcut = "replicatedSave()", returning = "savedDto")
    public void replicateSave(final JoinPoint joinPoint, final AbstractDto savedDto) {
        replicate(joinPoint, new SaveReplication(savedDto));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @AfterReturning(pointcut = "replicatedSaveAll()", returning = "savedDtos")
    public void replicateSaveAll(final JoinPoint joinPoint, final List savedDtos) {
        ((List<AbstractDto>) savedDtos).forEach(dto -> replicateSave(joinPoint, dto));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @AfterReturning(value = "replicatedUpdate()", returning = "updatedDto")
    public void replicateUpdate(final JoinPoint joinPoint, final AbstractDto updatedDto) {
        replicate(joinPoint, new UpdateReplication(updatedDto));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Around("replicatedDeleteById()")
    public Object replicateDeleteById(final ProceedingJoinPoint joinPoint)
            throws Throwable {
        final Object entityId = joinPoint.getArgs()[0];
        final Object result = joinPoint.proceed();
        replicate(joinPoint, new DeleteReplication(entityId));
        return result;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void replicate(final JoinPoint joinPoint, final Replication replication) {
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

    @Pointcut("replicatedCrudService() && save()")
    private void replicatedSave() {

    }

    @Pointcut("replicatedCrudService() && saveAll()")
    private void replicatedSaveAll() {

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
    private void save() {

    }

    @Pointcut("execution(public java.util.List *.saveAll(java.util.Collection))")
    private void saveAll() {

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
