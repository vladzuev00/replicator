package by.aurorasoft.replicator.aop;

import by.aurorasoft.replicator.aop.ProducingReplicationAspect.NoReplicationProducerException;
import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.aurorasoft.replicator.base.dto.TestDto;
import by.aurorasoft.replicator.base.service.FirstTestCRUDService;
import by.aurorasoft.replicator.holder.ReplicationProducerHolder;
import by.aurorasoft.replicator.model.produced.ProducedReplication;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static by.aurorasoft.replicator.util.ReplicationAssertUtil.assertProducingDelete;
import static by.aurorasoft.replicator.util.ReplicationAssertUtil.assertProducingSave;
import static java.util.Objects.requireNonNullElse;
import static java.util.Optional.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;
import static org.springframework.aop.framework.AopProxyUtils.getSingletonTarget;

public final class ProducingReplicationAspectTest extends AbstractSpringBootTest {

    @MockBean
    private ReplicationProducerHolder producerHolder;

    @Autowired
    private FirstTestCRUDService service;

    @Captor
    private ArgumentCaptor<ProducedReplication<Long>> replicationArgumentCaptor;

    @Test
    @SuppressWarnings("unchecked")
    public void createShouldBeReplicated() {
        final TestDto givenDto = new TestDto(255L);
        final ReplicationProducer<Long> givenProducer = mock(ReplicationProducer.class);

        when(producerHolder.findByService(same(unProxyService()))).thenReturn(Optional.of(givenProducer));

        final TestDto actual = service.save(givenDto);
        assertSame(givenDto, actual);

        verify(givenProducer, times(1)).send(replicationArgumentCaptor.capture());

        final ProducedReplication<Long> actualReplication = replicationArgumentCaptor.getValue();
        assertProducingSave(actualReplication, givenDto);
    }

    @Test(expected = NoReplicationProducerException.class)
    public void createShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final TestDto givenDto = new TestDto(255L);

        when(producerHolder.findByService(same(unProxyService()))).thenReturn(empty());

        service.save(givenDto);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void createAllShouldBeReplicated() {
        final TestDto firstGivenDto = new TestDto(255L);
        final TestDto secondGivenDto = new TestDto(256L);
        final List<TestDto> givenDtos = List.of(firstGivenDto, secondGivenDto);

        final ReplicationProducer<Long> givenProducer = mock(ReplicationProducer.class);

        when(producerHolder.findByService(same(unProxyService()))).thenReturn(Optional.of(givenProducer));

        final List<TestDto> actual = service.saveAll(givenDtos);
        assertEquals(givenDtos, actual);

        verify(givenProducer, times(givenDtos.size())).send(replicationArgumentCaptor.capture());

        final List<ProducedReplication<Long>> actualReplications = replicationArgumentCaptor.getAllValues();
        assertEquals(2, actualReplications.size());
        assertProducingSave(actualReplications.get(0), firstGivenDto);
        assertProducingSave(actualReplications.get(1), secondGivenDto);
    }

    @Test(expected = NoReplicationProducerException.class)
    public void createAllShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final List<TestDto> givenDtos = List.of(new TestDto(255L), new TestDto(256L));

        when(producerHolder.findByService(same(unProxyService()))).thenReturn(empty());

        service.saveAll(givenDtos);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void updateShouldBeReplicated() {
        final TestDto givenDto = new TestDto(255L);
        final ReplicationProducer<Long> givenProducer = mock(ReplicationProducer.class);

        when(producerHolder.findByService(same(unProxyService()))).thenReturn(Optional.of(givenProducer));

        final TestDto actual = service.update(givenDto);
        assertSame(givenDto, actual);

        verify(givenProducer, times(1)).send(replicationArgumentCaptor.capture());

        final ProducedReplication<Long> actualReplication = replicationArgumentCaptor.getValue();
        assertProducingSave(actualReplication, givenDto);
    }

    @Test(expected = NoReplicationProducerException.class)
    public void updateShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final TestDto givenDto = new TestDto(255L);

        when(producerHolder.findByService(same(unProxyService()))).thenReturn(empty());

        service.update(givenDto);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void partialUpdateShouldBeReplicated() {
        final Long givenId = 255L;
        final TestDto givenDto = new TestDto(givenId);

        final Object givenPartial = new Object();
        final ReplicationProducer<Long> givenProducer = mock(ReplicationProducer.class);

        when(producerHolder.findByService(same(unProxyService()))).thenReturn(Optional.of(givenProducer));

        final TestDto actual = service.updatePartial(givenId, givenPartial);
        assertEquals(givenDto, actual);

        verify(givenProducer, times(1)).send(replicationArgumentCaptor.capture());

        final ProducedReplication<Long> actualReplication = replicationArgumentCaptor.getValue();
        assertProducingSave(actualReplication, givenDto);
    }

    @Test(expected = NoReplicationProducerException.class)
    public void partialUpdateShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final Long givenId = 255L;
        final Object givenPartial = new Object();

        when(producerHolder.findByService(same(unProxyService()))).thenReturn(empty());

        service.updatePartial(givenId, givenPartial);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void deleteByIdShouldBeReplicated() {
        final Long givenId = 255L;

        final ReplicationProducer<Long> givenProducer = mock(ReplicationProducer.class);
        when(producerHolder.findByService(same(unProxyService()))).thenReturn(Optional.of(givenProducer));

        service.delete(givenId);

        verify(givenProducer, times(1)).send(replicationArgumentCaptor.capture());

        final ProducedReplication<Long> actualReplication = replicationArgumentCaptor.getValue();
        assertProducingDelete(actualReplication, givenId);
    }

    @Test(expected = NoReplicationProducerException.class)
    public void deleteByIdShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final Long givenId = 255L;

        when(producerHolder.findByService(same(unProxyService()))).thenReturn(empty());

        service.delete(givenId);
    }

    private FirstTestCRUDService unProxyService() {
        return (FirstTestCRUDService) requireNonNullElse(getSingletonTarget(service), service);
    }
}
