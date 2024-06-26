package by.aurorasoft.replicator.aop;

import by.aurorasoft.replicator.aop.RegisterReplicationAspect.NoReplicationProducerException;
import by.aurorasoft.replicator.aop.RegisterReplicationAspect.ReplicationCallback;
import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.aurorasoft.replicator.v1.dto.TestV1Dto;
import by.aurorasoft.replicator.v1.service.FirstTestV1CRUDService;
import by.aurorasoft.replicator.v2.dto.TestV2Dto;
import by.aurorasoft.replicator.v2.service.FirstTestV2CRUDService;
import by.aurorasoft.replicator.holder.producer.ReplicationProducerHolder;
import by.aurorasoft.replicator.model.replication.produced.DeleteProducedReplication;
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
import static java.util.Optional.of;
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

        final ReplicationProducer givenProducer = mockProducerFor(v1Service);

        final TestV1Dto actual = v1Service.save(givenDto);
        assertSame(givenDto, actual);

        verifyReplications(givenProducer, new SaveProducedReplication(actual));
    }

    @Test(expected = NoReplicationProducerException.class)
    public void createByV1ServiceShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final TestV1Dto givenDto = new TestV1Dto(255L);

        v1Service.save(givenDto);
    }

    @Test
    public void createByV2ServiceShouldBeReplicated() {
        final TestV2Dto givenDto = new TestV2Dto(255L);

        final ReplicationProducer givenProducer = mockProducerFor(v2Service);

        final TestV2Dto actual = v2Service.save(givenDto);
        assertSame(givenDto, actual);

        verifyReplications(givenProducer, new SaveProducedReplication(actual));
    }

    @Test(expected = NoReplicationProducerException.class)
    public void createByV2ServiceShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final TestV2Dto givenDto = new TestV2Dto(255L);

        v2Service.save(givenDto);
    }

    @Test
    public void createAllByV1ServiceShouldBeReplicated() {
        final List<TestV1Dto> givenDtos = List.of(new TestV1Dto(255L), new TestV1Dto(256L));

        final ReplicationProducer givenProducer = mockProducerFor(v1Service);

        final List<TestV1Dto> actual = v1Service.saveAll(givenDtos);
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

        v1Service.saveAll(givenDtos);
    }

    @Test
    public void createAllByV2ServiceShouldBeReplicated() {
        final List<TestV2Dto> givenDtos = List.of(new TestV2Dto(255L), new TestV2Dto(256L));

        final ReplicationProducer givenProducer = mockProducerFor(v2Service);

        final List<TestV2Dto> actual = v2Service.saveAll(givenDtos);
        assertEquals(givenDtos, actual);

        verifyReplications(
                givenProducer,
                new SaveProducedReplication(actual.get(0)),
                new SaveProducedReplication(actual.get(1))
        );
    }

    @Test(expected = NoReplicationProducerException.class)
    public void createAllByV2ServiceShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final List<TestV2Dto> givenDtos = List.of(new TestV2Dto(255L), new TestV2Dto(256L));

        v2Service.saveAll(givenDtos);
    }

    @Test
    public void updateByV1ServiceShouldBeReplicated() {
        final TestV1Dto givenDto = new TestV1Dto(255L);

        final ReplicationProducer givenProducer = mockProducerFor(v1Service);

        final TestV1Dto actual = v1Service.update(givenDto);
        assertSame(givenDto, actual);

        verifyReplications(givenProducer, new SaveProducedReplication(actual));
    }

    @Test(expected = NoReplicationProducerException.class)
    public void updateByV1ServiceShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final TestV1Dto givenDto = new TestV1Dto(255L);

        v1Service.update(givenDto);
    }

    @Test
    public void updateByV2ServiceShouldBeReplicated() {
        final TestV2Dto givenDto = new TestV2Dto(255L);

        final ReplicationProducer givenProducer = mockProducerFor(v2Service);

        final TestV2Dto actual = v2Service.update(givenDto);
        assertSame(givenDto, actual);

        verifyReplications(givenProducer, new SaveProducedReplication(actual));
    }

    @Test(expected = NoReplicationProducerException.class)
    public void updateByV2ServiceShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final TestV2Dto givenDto = new TestV2Dto(255L);

        v2Service.update(givenDto);
    }

    @Test
    public void partialUpdateByV1ServiceShouldBeReplicated() {
        final Long givenId = 255L;
        final Object givenPartial = new Object();

        final ReplicationProducer givenProducer = mockProducerFor(v1Service);

        final TestV1Dto actual = v1Service.updatePartial(givenId, givenPartial);
        final TestV1Dto expected = new TestV1Dto(givenId);
        assertEquals(expected, actual);

        verifyReplications(givenProducer, new SaveProducedReplication(actual));
    }

    @Test(expected = NoReplicationProducerException.class)
    public void partialUpdateByV1ServiceShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final Long givenId = 255L;
        final Object givenPartial = new Object();

        v1Service.updatePartial(givenId, givenPartial);
    }

    @Test
    public void partialUpdateByV2ServiceShouldBeReplicated() {
        final Long givenId = 255L;
        final Object givenPartial = new Object();

        final ReplicationProducer givenProducer = mockProducerFor(v2Service);

        final TestV2Dto actual = v2Service.updatePartial(givenId, givenPartial);
        final TestV2Dto expected = new TestV2Dto(givenId);
        assertEquals(expected, actual);

        verifyReplications(givenProducer, new SaveProducedReplication(actual));
    }

    @Test(expected = NoReplicationProducerException.class)
    public void partialUpdateByV2ServiceShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final Long givenId = 255L;
        final Object givenPartial = new Object();

        v2Service.updatePartial(givenId, givenPartial);
    }

    @Test
    public void deleteByIdByV1ServiceShouldBeReplicated() {
        final Long givenId = 255L;

        final ReplicationProducer givenProducer = mockProducerFor(v1Service);

        v1Service.deleteById(givenId);

        verifyReplications(givenProducer, new DeleteProducedReplication(givenId));
    }

    @Test(expected = NoReplicationProducerException.class)
    public void deleteByIdByV1ServiceShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final Long givenId = 255L;

        v1Service.deleteById(givenId);
    }

    @Test
    public void deleteByIdByV2ServiceShouldBeReplicated() {
        final Long givenId = 255L;

        final ReplicationProducer givenProducer = mockProducerFor(v2Service);

        v2Service.delete(givenId);

        verifyReplications(givenProducer, new DeleteProducedReplication(givenId));
    }

    @Test(expected = NoReplicationProducerException.class)
    public void deleteByIdByV2ServiceShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final Long givenId = 255L;

        v2Service.delete(givenId);
    }

    @Test
    public void deleteAllByV1ServiceShouldBeReplicated() {
        final Long firstGivenDtoId = 255L;
        final Long secondGivenDtoId = 256L;
        final List<TestV1Dto> givenDtos = List.of(new TestV1Dto(firstGivenDtoId), new TestV1Dto(secondGivenDtoId));

        final ReplicationProducer givenProducer = mockProducerFor(v1Service);

        v1Service.deleteAll(givenDtos);

        verifyReplications(
                givenProducer,
                new DeleteProducedReplication(firstGivenDtoId),
                new DeleteProducedReplication(secondGivenDtoId)
        );
    }

    @Test(expected = NoReplicationProducerException.class)
    public void deleteAllByV1ServiceShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final List<TestV1Dto> givenDtos = List.of(new TestV1Dto(255L), new TestV1Dto(256L));

        v1Service.deleteAll(givenDtos);
    }

    private ReplicationProducer mockProducerFor(final Object service) {
        final ReplicationProducer producer = mock(ReplicationProducer.class);
        when(mockedProducerHolder.findForService(same(unProxy(service)))).thenReturn(of(producer));
        return producer;
    }

    private Object unProxy(final Object object) {
        return requireNonNullElse(getSingletonTarget(object), object);
    }

    private void verifyReplications(final ReplicationProducer producer, final ProducedReplication... replications) {
        mockedTransactionManager
                .verify(() -> registerSynchronization(callbackArgumentCaptor.capture()), times(replications.length));
        final List<ReplicationCallback> actual = callbackArgumentCaptor.getAllValues();
        final List<ReplicationCallback> expected = createCallbacks(producer, replications);
        checkEquals(expected, actual);
    }

    private List<ReplicationCallback> createCallbacks(final ReplicationProducer producer,
                                                      final ProducedReplication... replications) {
        return stream(replications)
                .map(replication -> new ReplicationCallback(producer, replication))
                .toList();
    }

    private void checkEquals(final List<ReplicationCallback> expected, final List<ReplicationCallback> actual) {
        assertEquals(expected.size(), actual.size());
        range(0, expected.size()).forEach(i -> checkEquals(expected.get(i), actual.get(i)));
    }

    private void checkEquals(final ReplicationCallback expected, final ReplicationCallback actual) {
        assertSame(expected.getProducer(), actual.getProducer());
        assertEquals(expected.getReplication(), actual.getReplication());
    }
}
