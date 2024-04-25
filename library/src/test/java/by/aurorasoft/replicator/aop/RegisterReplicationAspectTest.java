package by.aurorasoft.replicator.aop;

import by.aurorasoft.replicator.aop.RegisterReplicationAspect.ReplicationCallback;
import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.aurorasoft.replicator.base.v1.dto.TestV1Dto;
import by.aurorasoft.replicator.base.v1.service.FirstTestV1CRUDService;
import by.aurorasoft.replicator.base.v2.service.FirstTestV2CRUDService;
import by.aurorasoft.replicator.holder.ReplicationProducerHolder;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
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
    private FirstTestV1CRUDService v1Service;

    @Autowired
    private FirstTestV2CRUDService v2Service;

    private MockedStatic<TransactionSynchronizationManager> mockedTransactionManager;

    @Captor
    private ArgumentCaptor<ReplicationCallback> callbackArgumentCaptor;

    @Before
    public void mockTransactionManager() {
        mockedTransactionManager = mockStatic(TransactionSynchronizationManager.class);
    }

    @After
    public void closeTransactionManager() {
        mockedTransactionManager.close();
    }

    @Test
    public void createByV1ServiceShouldBeReplicated() {
        final TestV1Dto givenDto = new TestV1Dto(255L);
        final ReplicationProducer givenProducer = createProducerBoundedWithService(v1Service);

        final TestV1Dto actual = v1Service.save(givenDto);
        assertSame(givenDto, actual);

        verifyRegistrationSaveReplication(actual, givenProducer);
    }

//    @Test
//    public void createShouldBeReplicated() {
//        final TestDto givenDto = new TestDto(255L);
//        final ReplicationProducer<Long> givenProducer = createProducerBoundedWithService();
//
//        final TestDto actual = v2Service.save(givenDto);
//        assertSame(givenDto, actual);
//
//        verifyRegistrationSaveReplication(actual, givenProducer);
//    }

//    @Test(expected = NoReplicationProducerException.class)
//    public void createShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
//        final TestDto givenDto = new TestDto(255L);
//
//        bindProducerWithService(null);
//
//        v2Service.save(givenDto);
//    }
//
//    @Test
//    public void createAllShouldBeReplicated() {
//        final TestDto firstGivenDto = new TestDto(255L);
//        final TestDto secondGivenDto = new TestDto(256L);
//        final List<TestDto> givenDtos = List.of(firstGivenDto, secondGivenDto);
//
//        final ReplicationProducer<Long> givenProducer = createProducerBoundedWithService();
//
//        final List<TestDto> actual = v2Service.saveAll(givenDtos);
//        assertEquals(givenDtos, actual);
//
//        verifyRegistrationSaveReplications(actual, givenProducer);
//    }
//
//    @Test(expected = NoReplicationProducerException.class)
//    public void createAllShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
//        final List<TestDto> givenDtos = List.of(new TestDto(255L), new TestDto(256L));
//
//        bindProducerWithService(null);
//
//        v2Service.saveAll(givenDtos);
//    }
//
//    @Test
//    public void updateShouldBeReplicated() {
//        final TestDto givenDto = new TestDto(255L);
//        final ReplicationProducer<Long> givenProducer = createProducerBoundedWithService();
//
//        final TestDto actual = v2Service.update(givenDto);
//        assertSame(givenDto, actual);
//
//        verifyRegistrationSaveReplication(actual, givenProducer);
//    }
//
//    @Test(expected = NoReplicationProducerException.class)
//    public void updateShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
//        final TestDto givenDto = new TestDto(255L);
//
//        bindProducerWithService(null);
//
//        v2Service.update(givenDto);
//    }
//
//    @Test
//    public void partialUpdateShouldBeReplicated() {
//        final Long givenId = 255L;
//        final Object givenPartial = new Object();
//        final ReplicationProducer<Long> givenProducer = createProducerBoundedWithService();
//
//        final TestDto actual = v2Service.updatePartial(givenId, givenPartial);
//        final TestDto expected = new TestDto(givenId);
//        assertEquals(expected, actual);
//
//        verifyRegistrationSaveReplication(actual, givenProducer);
//    }
//
//    @Test(expected = NoReplicationProducerException.class)
//    public void partialUpdateShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
//        final Long givenId = 255L;
//        final Object givenPartial = new Object();
//
//        bindProducerWithService(null);
//
//        v2Service.updatePartial(givenId, givenPartial);
//    }
//
//    @Test
//    public void deleteByIdShouldBeReplicated() {
//        final Long givenId = 255L;
//        final ReplicationProducer<Long> givenProducer = createProducerBoundedWithService();
//
//        v2Service.delete(givenId);
//
//        verifyRegistrationDeleteReplication(givenId, givenProducer);
//    }
//
//    @Test(expected = NoReplicationProducerException.class)
//    public void deleteByIdShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
//        final Long givenId = 255L;
//
//        bindProducerWithService(null);
//
//        v2Service.delete(givenId);
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    public void replicationShouldBeSent() {
//        final ReplicationProducer<Long> givenProducer = mock(ReplicationProducer.class);
//        final ProducedReplication<Long> givenReplication = mock(ProducedReplication.class);
//        final ReplicationCallback<Long> givenCallback = new ReplicationCallback<>(givenProducer, givenReplication);
//
//        givenCallback.afterCommit();
//
//        verify(givenProducer, times(1)).send(same(givenReplication));
//    }

    private ReplicationProducer createProducerBoundedWithService(final Object service) {
        final ReplicationProducer producer = mock(ReplicationProducer.class);
        bindProducerWithService(producer, service);
        return producer;
    }

    private void bindProducerWithService(final ReplicationProducer producer, final Object service) {
        when(mockedProducerHolder.findByService(same(unProxy(service)))).thenReturn(ofNullable(producer));
    }

    private Object unProxy(final Object object) {
        return requireNonNullElse(getSingletonTarget(object), object);
    }

    private void verifyRegistrationSaveReplication(final Object dto, final ReplicationProducer producer) {
        verifyRegistrationReplication(new SaveProducedReplication(dto), producer);
    }

    //    private void verifyRegistrationSaveReplications(final List<TestDto> dtos,
//                                                    final ReplicationProducer<Long> producer) {
//        verifyRegistrationReplications(createSaveReplications(dtos), producer);
//    }
//
//    private void verifyRegistrationDeleteReplication(final Long entityId, final ReplicationProducer<Long> producer) {
//        verifyRegistrationReplication(new DeleteProducedReplication<>(entityId), producer);
//    }
//
    private void verifyRegistrationReplication(final ProducedReplication replication, final ReplicationProducer producer) {
        final List<ProducedReplication> replications = singletonList(replication);
        verifyRegistrationReplications(replications, producer);
    }

    private void verifyRegistrationReplications(final List<? extends ProducedReplication> replications,
                                                final ReplicationProducer producer) {
        mockedTransactionManager
                .verify(() -> registerSynchronization(callbackArgumentCaptor.capture()), times(replications.size()));
        final List<ReplicationCallback> actualCallbacks = callbackArgumentCaptor.getAllValues();
        final List<ReplicationCallback> expectedCallbacks = createCallbacks(replications, producer);
        checkEquals(expectedCallbacks, actualCallbacks);
    }

    private static List<ReplicationCallback> createCallbacks(final List<? extends ProducedReplication> replications,
                                                             final ReplicationProducer producer) {
        return replications.stream()
                .map(replication -> new ReplicationCallback(producer, replication))
                .toList();
    }

    private static void checkEquals(final List<ReplicationCallback> expected, final List<ReplicationCallback> actual) {
        assertEquals(expected.size(), actual.size());
        range(0, expected.size()).forEach(i -> checkEquals(expected.get(i), actual.get(i)));
    }

    private static void checkEquals(final ReplicationCallback expected, final ReplicationCallback actual) {
        assertSame(expected.getProducer(), actual.getProducer());
        assertEquals(expected.getReplication(), actual.getReplication());
    }
//
//    private static List<SaveProducedReplication<Long, TestDto>> createSaveReplications(final List<TestDto> dtos) {
//        return dtos.stream()
//                .map(SaveProducedReplication::new)
//                .toList();
//    }
}
