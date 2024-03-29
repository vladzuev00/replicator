package by.aurorasoft.replicator.aop;

import by.aurorasoft.replicator.aop.RegisterReplicationAspect.NoReplicationProducerException;
import by.aurorasoft.replicator.aop.RegisterReplicationAspect.ReplicationCallback;
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

import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNullElse;
import static java.util.Optional.ofNullable;
import static java.util.stream.IntStream.range;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;
import static org.springframework.aop.framework.AopProxyUtils.getSingletonTarget;
import static org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization;

public final class RegisterReplicationAspectTest extends AbstractSpringBootTest {

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

        bindProducerForService(givenProducer);

        final TestDto actual = service.save(givenDto);
        assertSame(givenDto, actual);

        verifyRegistrationSaveReplication(givenDto, givenProducer);
    }

    @Test(expected = NoReplicationProducerException.class)
    public void createShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final TestDto givenDto = new TestDto(255L);

        bindProducerForService(null);

        service.save(givenDto);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void createAllShouldBeReplicated() {
        final TestDto firstGivenDto = new TestDto(255L);
        final TestDto secondGivenDto = new TestDto(256L);
        final List<TestDto> givenDtos = List.of(firstGivenDto, secondGivenDto);

        final ReplicationProducer<Long> givenProducer = mock(ReplicationProducer.class);

        bindProducerForService(givenProducer);

        final List<TestDto> actual = service.saveAll(givenDtos);
        assertEquals(givenDtos, actual);

        verifyRegistrationSaveReplications(givenDtos, givenProducer);
    }

    @Test(expected = NoReplicationProducerException.class)
    public void createAllShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final List<TestDto> givenDtos = List.of(new TestDto(255L), new TestDto(256L));

        bindProducerForService(null);

        service.saveAll(givenDtos);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void updateShouldBeReplicated() {
        final TestDto givenDto = new TestDto(255L);
        final ReplicationProducer<Long> givenProducer = mock(ReplicationProducer.class);

        bindProducerForService(givenProducer);

        final TestDto actual = service.update(givenDto);
        assertSame(givenDto, actual);

        verifyRegistrationSaveReplication(givenDto, givenProducer);
    }

    @Test(expected = NoReplicationProducerException.class)
    public void updateShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final TestDto givenDto = new TestDto(255L);

        bindProducerForService(null);

        service.update(givenDto);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void partialUpdateShouldBeReplicated() {
        final Long givenId = 255L;
        final TestDto givenDto = new TestDto(givenId);

        final Object givenPartial = new Object();
        final ReplicationProducer<Long> givenProducer = mock(ReplicationProducer.class);

        bindProducerForService(givenProducer);

        final TestDto actual = service.updatePartial(givenId, givenPartial);
        assertEquals(givenDto, actual);

        verifyRegistrationSaveReplication(givenDto, givenProducer);
    }

    @Test(expected = NoReplicationProducerException.class)
    public void partialUpdateShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final Long givenId = 255L;
        final Object givenPartial = new Object();

        bindProducerForService(null);

        service.updatePartial(givenId, givenPartial);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void deleteByIdShouldBeReplicated() {
        final Long givenId = 255L;
        final ReplicationProducer<Long> givenProducer = mock(ReplicationProducer.class);

        bindProducerForService(givenProducer);

        service.delete(givenId);

        verifyRegistrationDeleteReplication(givenId, givenProducer);
    }

    @Test(expected = NoReplicationProducerException.class)
    public void deleteByIdShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final Long givenId = 255L;

        bindProducerForService(null);

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

    private void bindProducerForService(final ReplicationProducer<Long> producer) {
        when(mockedProducerHolder.findByService(same(unProxyService()))).thenReturn(ofNullable(producer));
    }

    private FirstTestCRUDService unProxyService() {
        return (FirstTestCRUDService) requireNonNullElse(getSingletonTarget(service), service);
    }

    private void verifyRegistrationSaveReplication(final TestDto dto, final ReplicationProducer<Long> producer) {
        verifyRegistrationReplication(new SaveProducedReplication<>(dto), producer);
    }

    private void verifyRegistrationSaveReplications(final List<TestDto> dtos, final ReplicationProducer<Long> producer) {
        verifyRegistrationReplications(createSaveReplications(dtos), producer);
    }

    private void verifyRegistrationDeleteReplication(final Long entityId, final ReplicationProducer<Long> producer) {
        verifyRegistrationReplication(new DeleteProducedReplication<>(entityId), producer);
    }

    private void verifyRegistrationReplication(final ProducedReplication<Long> replication,
                                               final ReplicationProducer<Long> producer) {
        final List<ProducedReplication<Long>> replications = singletonList(replication);
        verifyRegistrationReplications(replications, producer);
    }

    private void verifyRegistrationReplications(final List<? extends ProducedReplication<Long>> replications,
                                                final ReplicationProducer<Long> producer) {
        mockedTransactionManager.verify(() -> registerSynchronization(callbackArgumentCaptor.capture()), times(replications.size()));
        final List<ReplicationCallback<Long>> actualCallbacks = callbackArgumentCaptor.getAllValues();
        final List<ReplicationCallback<Long>> expectedCallbacks = createCallbacks(replications, producer);
        checkEquals(expectedCallbacks, actualCallbacks);
    }

    private static List<ReplicationCallback<Long>> createCallbacks(final List<? extends ProducedReplication<Long>> replications,
                                                                   final ReplicationProducer<Long> producer) {
        return replications.stream()
                .map(replication -> new ReplicationCallback<>(producer, replication))
                .toList();
    }

    private static void checkEquals(final List<ReplicationCallback<Long>> expected,
                                    final List<ReplicationCallback<Long>> actual) {
        assertEquals(expected.size(), actual.size());
        range(0, expected.size()).forEach(i -> checkEquals(expected.get(i), actual.get(i)));
    }

    private static void checkEquals(final ReplicationCallback<Long> expected, final ReplicationCallback<Long> actual) {
        assertSame(expected.getProducer(), actual.getProducer());
        assertEquals(expected.getReplication(), actual.getReplication());
    }

    private static List<SaveProducedReplication<Long, TestDto>> createSaveReplications(final List<TestDto> dtos) {
        return dtos.stream()
                .map(SaveProducedReplication::new)
                .toList();
    }
}
