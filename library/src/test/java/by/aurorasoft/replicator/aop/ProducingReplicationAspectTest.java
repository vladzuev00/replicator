package by.aurorasoft.replicator.aop;

import by.aurorasoft.replicator.aop.ProducingReplicationAspect.NoReplicationProducerException;
import by.aurorasoft.replicator.aop.ProducingReplicationAspect.ReplicationCallback;
import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.aurorasoft.replicator.base.dto.TestDto;
import by.aurorasoft.replicator.base.service.FirstTestCRUDService;
import by.aurorasoft.replicator.holder.ReplicationProducerHolder;
import by.aurorasoft.replicator.model.produced.DeleteProducedReplication;
import by.aurorasoft.replicator.model.produced.ProducedReplication;
import by.aurorasoft.replicator.model.produced.SaveProducedReplication;
import by.aurorasoft.replicator.producer.ReplicationProducer;
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
import static java.util.stream.IntStream.range;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;
import static org.springframework.aop.framework.AopProxyUtils.getSingletonTarget;
import static org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization;

public final class ProducingReplicationAspectTest extends AbstractSpringBootTest {

    @MockBean
    private ReplicationProducerHolder mockedProducerHolder;

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

        when(mockedProducerHolder.findByService(same(unProxyService()))).thenReturn(Optional.of(givenProducer));

        final TestDto actual = service.save(givenDto);
        assertSame(givenDto, actual);

        mockedTransactionManager.verify(() -> registerSynchronization(callbackArgumentCaptor.capture()), times(1));

        final ReplicationCallback<Long> actualCallback = callbackArgumentCaptor.getValue();
        final ReplicationCallback<Long> expectedCallback = new ReplicationCallback<>(
                givenProducer,
                new SaveProducedReplication<>(actual)
        );
        checkEquals(expectedCallback, actualCallback);
    }

    @Test(expected = NoReplicationProducerException.class)
    public void createShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final TestDto givenDto = new TestDto(255L);

        when(mockedProducerHolder.findByService(same(unProxyService()))).thenReturn(empty());

        service.save(givenDto);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void createAllShouldBeReplicated() {
        final TestDto firstGivenDto = new TestDto(255L);
        final TestDto secondGivenDto = new TestDto(256L);
        final List<TestDto> givenDtos = List.of(firstGivenDto, secondGivenDto);

        final ReplicationProducer<Long> givenProducer = mock(ReplicationProducer.class);

        when(mockedProducerHolder.findByService(same(unProxyService()))).thenReturn(Optional.of(givenProducer));

        final List<TestDto> actual = service.saveAll(givenDtos);
        assertEquals(givenDtos, actual);

        mockedTransactionManager.verify(() -> registerSynchronization(callbackArgumentCaptor.capture()), times(2));

        final List<ReplicationCallback<Long>> actualCallbacks = callbackArgumentCaptor.getAllValues();
        final List<ReplicationCallback<Long>> expectedCallbacks = List.of(
                new ReplicationCallback<>(givenProducer, new SaveProducedReplication<>(actual.get(0))),
                new ReplicationCallback<>(givenProducer, new SaveProducedReplication<>(actual.get(1)))
        );
        checkEquals(expectedCallbacks, actualCallbacks);
    }

    @Test(expected = NoReplicationProducerException.class)
    public void createAllShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final List<TestDto> givenDtos = List.of(new TestDto(255L), new TestDto(256L));

        when(mockedProducerHolder.findByService(same(unProxyService()))).thenReturn(empty());

        service.saveAll(givenDtos);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void updateShouldBeReplicated() {
        final TestDto givenDto = new TestDto(255L);
        final ReplicationProducer<Long> givenProducer = mock(ReplicationProducer.class);

        when(mockedProducerHolder.findByService(same(unProxyService()))).thenReturn(Optional.of(givenProducer));

        final TestDto actual = service.update(givenDto);
        assertSame(givenDto, actual);

        mockedTransactionManager.verify(() -> registerSynchronization(callbackArgumentCaptor.capture()), times(1));

        final ReplicationCallback<Long> actualCallback = callbackArgumentCaptor.getValue();
        final ReplicationCallback<Long> expectedCallback = new ReplicationCallback<>(
                givenProducer,
                new SaveProducedReplication<>(actual)
        );
        checkEquals(expectedCallback, actualCallback);
    }

    @Test(expected = NoReplicationProducerException.class)
    public void updateShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final TestDto givenDto = new TestDto(255L);

        when(mockedProducerHolder.findByService(same(unProxyService()))).thenReturn(empty());

        service.update(givenDto);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void partialUpdateShouldBeReplicated() {
        final Long givenId = 255L;
        final TestDto givenDto = new TestDto(givenId);

        final Object givenPartial = new Object();
        final ReplicationProducer<Long> givenProducer = mock(ReplicationProducer.class);

        when(mockedProducerHolder.findByService(same(unProxyService()))).thenReturn(Optional.of(givenProducer));

        final TestDto actual = service.updatePartial(givenId, givenPartial);
        assertEquals(givenDto, actual);

        mockedTransactionManager.verify(() -> registerSynchronization(callbackArgumentCaptor.capture()), times(1));

        final ReplicationCallback<Long> actualCallback = callbackArgumentCaptor.getValue();
        final ReplicationCallback<Long> expectedCallback = new ReplicationCallback<>(
                givenProducer,
                new SaveProducedReplication<>(actual)
        );
        checkEquals(expectedCallback, actualCallback);
    }

    @Test(expected = NoReplicationProducerException.class)
    public void partialUpdateShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final Long givenId = 255L;
        final Object givenPartial = new Object();

        when(mockedProducerHolder.findByService(same(unProxyService()))).thenReturn(empty());

        service.updatePartial(givenId, givenPartial);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void deleteByIdShouldBeReplicated() {
        final Long givenId = 255L;

        final ReplicationProducer<Long> givenProducer = mock(ReplicationProducer.class);
        when(mockedProducerHolder.findByService(same(unProxyService()))).thenReturn(Optional.of(givenProducer));

        service.delete(givenId);

        mockedTransactionManager.verify(() -> registerSynchronization(callbackArgumentCaptor.capture()), times(1));

        final ReplicationCallback<Long> actualCallback = callbackArgumentCaptor.getValue();
        final ReplicationCallback<Long> expectedCallback = new ReplicationCallback<>(
                givenProducer,
                new DeleteProducedReplication<>(givenId)
        );
        checkEquals(expectedCallback, actualCallback);
    }

    @Test(expected = NoReplicationProducerException.class)
    public void deleteByIdShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final Long givenId = 255L;

        when(mockedProducerHolder.findByService(same(unProxyService()))).thenReturn(empty());

        service.delete(givenId);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void replicationShouldBeSent() {
        final ReplicationProducer<Long> givenProducer = mock(ReplicationProducer.class);
        final ProducedReplication<Long> givenReplication = mock(ProducedReplication.class);
        final ReplicationCallback<Long> givenCallback = new ReplicationCallback<>(givenProducer, givenReplication);

        givenCallback.afterCommit();

        verify(givenProducer, times(1)).send(same(givenReplication));
    }

    private FirstTestCRUDService unProxyService() {
        return (FirstTestCRUDService) requireNonNullElse(getSingletonTarget(service), service);
    }

    private static void checkEquals(final ReplicationCallback<Long> expected, final ReplicationCallback<Long> actual) {
        assertSame(expected.getProducer(), actual.getProducer());
        assertEquals(expected.getReplication(), actual.getReplication());
    }

    private static void checkEquals(final List<ReplicationCallback<Long>> expected,
                                    final List<ReplicationCallback<Long>> actual) {
        assertEquals(expected.size(), actual.size());
        range(0, expected.size()).forEach(i -> checkEquals(expected.get(i), actual.get(i)));
    }
}
