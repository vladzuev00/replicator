package by.aurorasoft.replicator.aop;

import by.aurorasoft.replicator.aop.RegisterReplicationAspect.NoReplicationProducerException;
import by.aurorasoft.replicator.aop.RegisterReplicationAspect.ReplicationCallback;
import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.aurorasoft.replicator.base.v1.dto.TestV1Dto;
import by.aurorasoft.replicator.base.v1.service.FirstTestV1CRUDService;
import by.aurorasoft.replicator.base.v2.dto.TestV2Dto;
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

import static java.util.Arrays.stream;
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
        final ReplicationProducer givenProducer = createProducerForService(v1Service);

        final TestV1Dto actual = v1Service.save(givenDto);
        assertSame(givenDto, actual);

        verifyReplications(givenProducer, new SaveProducedReplication(actual));
    }

    @Test
    public void createByV2ServiceShouldBeReplicated() {
        final TestV2Dto givenDto = new TestV2Dto(255L);
        final ReplicationProducer givenProducer = createProducerForService(v2Service);

        final TestV2Dto actual = v2Service.save(givenDto);
        assertSame(givenDto, actual);

        verifyReplications(givenProducer, new SaveProducedReplication(actual));
    }

    @Test(expected = NoReplicationProducerException.class)
    public void createByV1ServiceShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final TestV1Dto givenDto = new TestV1Dto(255L);

        leaveServiceWithoutProducer(v1Service);

        v1Service.save(givenDto);
    }

    @Test(expected = NoReplicationProducerException.class)
    public void createByV2ServiceShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final TestV2Dto givenDto = new TestV2Dto(255L);

        leaveServiceWithoutProducer(v2Service);

        v2Service.save(givenDto);
    }

    @Test
    public void createAllByV1ServiceShouldBeReplicated() {
        final List<TestV1Dto> givenDtos = List.of(new TestV1Dto(255L), new TestV1Dto(256L));
        final ReplicationProducer givenProducer = createProducerForService(v1Service);

        final List<TestV1Dto> actual = v1Service.saveAll(givenDtos);
        assertEquals(givenDtos, actual);

        verifyReplications(
                givenProducer,
                new SaveProducedReplication(actual.get(0)),
                new SaveProducedReplication(actual.get(1))
        );
    }

    @Test
    public void createAllByV2ServiceShouldBeReplicated() {
        final List<TestV2Dto> givenDtos = List.of(new TestV2Dto(255L), new TestV2Dto(256L));
        final ReplicationProducer givenProducer = createProducerForService(v2Service);

        final List<TestV2Dto> actual = v2Service.saveAll(givenDtos);
        assertEquals(givenDtos, actual);

        verifyReplications(
                givenProducer,
                new SaveProducedReplication(actual.get(0)),
                new SaveProducedReplication(actual.get(1))
        );
    }

    @Test(expected = NoReplicationProducerException.class)
    public void createAllByV1ServiceShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final List<TestV1Dto> givenDtos = List.of(new TestV1Dto(255L), new TestV1Dto(256L));

        leaveServiceWithoutProducer(v1Service);

        v1Service.saveAll(givenDtos);
    }

    @Test(expected = NoReplicationProducerException.class)
    public void createAllByV2ServiceShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final List<TestV2Dto> givenDtos = List.of(new TestV2Dto(255L), new TestV2Dto(256L));

        leaveServiceWithoutProducer(v2Service);

        v2Service.saveAll(givenDtos);
    }

    @Test
    public void updateByV1ServiceShouldBeReplicated() {
        final TestV1Dto givenDto = new TestV1Dto(255L);
        final ReplicationProducer givenProducer = createProducerForService(v1Service);

        final TestV1Dto actual = v1Service.update(givenDto);
        assertSame(givenDto, actual);

        verifyReplications(givenProducer, new SaveProducedReplication(actual));
    }

    @Test
    public void updateByV2ServiceShouldBeReplicated() {
        final TestV2Dto givenDto = new TestV2Dto(255L);
        final ReplicationProducer givenProducer = createProducerForService(v2Service);

        final TestV2Dto actual = v2Service.update(givenDto);
        assertSame(givenDto, actual);

        verifyReplications(givenProducer, new SaveProducedReplication(actual));
    }

    @Test(expected = NoReplicationProducerException.class)
    public void updateByV1ServiceShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final TestV1Dto givenDto = new TestV1Dto(255L);

        leaveServiceWithoutProducer(v1Service);

        v1Service.update(givenDto);
    }

    @Test(expected = NoReplicationProducerException.class)
    public void updateByV2ServiceShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final TestV2Dto givenDto = new TestV2Dto(255L);

        leaveServiceWithoutProducer(v2Service);

        v2Service.update(givenDto);
    }

    @Test
    public void partialUpdateByV1ServiceShouldBeReplicated() {
        final Long givenId = 255L;
        final Object givenPartial = new Object();
        final ReplicationProducer givenProducer = createProducerForService(v1Service);

        final TestV1Dto actual = v1Service.updatePartial(givenId, givenPartial);
        final TestV1Dto expected = new TestV1Dto(givenId);
        assertEquals(expected, actual);

        verifyReplications(givenProducer, new SaveProducedReplication(actual));
    }

    @Test
    public void partialUpdateByV2ServiceShouldBeReplicated() {
        final Long givenId = 255L;
        final Object givenPartial = new Object();
        final ReplicationProducer givenProducer = createProducerForService(v2Service);

        final TestV2Dto actual = v2Service.updatePartial(givenId, givenPartial);
        final TestV2Dto expected = new TestV2Dto(givenId);
        assertEquals(expected, actual);

        verifyReplications(givenProducer, new SaveProducedReplication(actual));
    }

    @Test(expected = NoReplicationProducerException.class)
    public void partialUpdateByV1ServiceNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final Long givenId = 255L;
        final Object givenPartial = new Object();

        leaveServiceWithoutProducer(v1Service);

        v1Service.updatePartial(givenId, givenPartial);
    }

    @Test(expected = NoReplicationProducerException.class)
    public void partialUpdateByV2ServiceNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final Long givenId = 255L;
        final Object givenPartial = new Object();

        leaveServiceWithoutProducer(v2Service);

        v2Service.updatePartial(givenId, givenPartial);
    }

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

    private ReplicationProducer createProducerForService(final Object service) {
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

    private void leaveServiceWithoutProducer(final Object service) {
        bindProducerWithService(null, service);
    }

    private void verifyReplications(final ReplicationProducer producer, final ProducedReplication... replications) {
        mockedTransactionManager
                .verify(() -> registerSynchronization(callbackArgumentCaptor.capture()), times(replications.length));
        final List<ReplicationCallback> actual = callbackArgumentCaptor.getAllValues();
        final List<ReplicationCallback> expected = createCallbacks(producer, replications);
        checkEquals(expected, actual);
    }

    private static List<ReplicationCallback> createCallbacks(final ReplicationProducer producer,
                                                             final ProducedReplication... replications) {
        return stream(replications)
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
}
