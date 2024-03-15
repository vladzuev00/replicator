package by.aurorasoft.replicator.aop;

import by.aurorasoft.replicator.aop.ProducingReplicationAspect.NoReplicationProducerException;
import by.aurorasoft.replicator.aop.ProducingReplicationAspect.ReplicationCallback;
import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.aurorasoft.replicator.base.dto.TestDto;
import by.aurorasoft.replicator.base.service.FirstTestCRUDService;
import by.aurorasoft.replicator.holder.ReplicationProducerHolder;
import by.aurorasoft.replicator.model.produced.ProducedReplication;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.util.ReplicationAssertUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNullElse;
import static java.util.Optional.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;
import static org.springframework.aop.framework.AopProxyUtils.getSingletonTarget;
import static org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization;

public final class ProducingReplicationAspectTest extends AbstractSpringBootTest {

    @MockBean
    private ReplicationProducerHolder producerHolder;

    @Autowired
    private FirstTestCRUDService service;

    private MockedStatic<TransactionSynchronizationManager> mockedTransactionManager;

    @Captor
    private ArgumentCaptor<ReplicationCallback<Long>> callbackArgumentCaptor;

    @Before
    public void mockTransactionManager() {
        mockedTransactionManager = mockStatic(TransactionSynchronizationManager.class);
    }

    @After
    public void closeTransactionManager() {
        mockedTransactionManager.close();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void createShouldBeReplicated() {
        final TestDto givenDto = new TestDto(255L);
        final ReplicationProducer<Long> givenProducer = mock(ReplicationProducer.class);

        when(producerHolder.findByService(same(unProxyService()))).thenReturn(Optional.of(givenProducer));

        final TestDto actual = service.save(givenDto);
        assertSame(givenDto, actual);

        mockedTransactionManager.verify(() -> registerSynchronization(callbackArgumentCaptor.capture()), times(1));

        final ReplicationCallback<Long> actualCallback = callbackArgumentCaptor.getValue();
        assertSave(actualCallback, givenProducer, givenDto);
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

        mockedTransactionManager.verify(() -> registerSynchronization(callbackArgumentCaptor.capture()), times(2));

        final List<ReplicationCallback<Long>> actualCallbacks = callbackArgumentCaptor.getAllValues();
        assertSave(actualCallbacks.get(0), givenProducer, firstGivenDto);
        assertSave(actualCallbacks.get(1), givenProducer, secondGivenDto);
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

        mockedTransactionManager.verify(() -> registerSynchronization(callbackArgumentCaptor.capture()), times(1));

        final ReplicationCallback<Long> actualCallback = callbackArgumentCaptor.getValue();
        assertSave(actualCallback, givenProducer, givenDto);
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

        mockedTransactionManager.verify(() -> registerSynchronization(callbackArgumentCaptor.capture()), times(1));

        final ReplicationCallback<Long> actualCallback = callbackArgumentCaptor.getValue();
        assertSave(actualCallback, givenProducer, givenDto);
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

        mockedTransactionManager.verify(() -> registerSynchronization(callbackArgumentCaptor.capture()), times(1));

        final ReplicationCallback<Long> actualCallback = callbackArgumentCaptor.getValue();
        assertDelete(actualCallback, givenProducer, givenId);
    }

    @Test(expected = NoReplicationProducerException.class)
    public void deleteByIdShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final Long givenId = 255L;

        when(producerHolder.findByService(same(unProxyService()))).thenReturn(empty());

        service.delete(givenId);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void replicationShouldBeSent() {
        final ReplicationProducer<Long> givenProducer = mock(ReplicationProducer.class);
        final ProducedReplication<Long> givenReplication = mock(ProducedReplication.class);
        final ReplicationCallback<Long> givenCallback = new ReplicationCallback<>(
                givenProducer,
                givenReplication
        );

        givenCallback.afterCommit();

        verify(givenProducer, times(1)).send(same(givenReplication));
    }

    private FirstTestCRUDService unProxyService() {
        return (FirstTestCRUDService) requireNonNullElse(getSingletonTarget(service), service);
    }

    private static void assertSave(final ReplicationCallback<Long> actual,
                                   final ReplicationProducer<Long> expectedProducer,
                                   final TestDto expectedDto) {
        assertProducer(actual, expectedProducer);
        ReplicationAssertUtil.assertSave(actual.getReplication(), expectedDto);
    }

    private static void assertDelete(final ReplicationCallback<Long> actual,
                                     final ReplicationProducer<Long> expectedProducer,
                                     final Long expectedEntityId) {
        assertProducer(actual, expectedProducer);
        ReplicationAssertUtil.assertDelete(actual.getReplication(), expectedEntityId);
    }

    private static void assertProducer(final ReplicationCallback<Long> actual,
                                       final ReplicationProducer<Long> expectedProducer) {
        assertSame(expectedProducer, actual.getProducer());
    }
}
