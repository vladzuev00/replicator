package by.aurorasoft.replicator.aop;

import by.aurorasoft.replicator.aop.ProducingReplicationAspect.ReplicationCallback;
import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.aurorasoft.replicator.factory.SaveProducedReplicationFactory;
import by.aurorasoft.replicator.model.replication.produced.DeleteProducedReplication;
import by.aurorasoft.replicator.model.replication.produced.ProducedReplication;
import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
import by.aurorasoft.replicator.v1.dto.TestV1Dto;
import by.aurorasoft.replicator.v1.service.FirstTestV1CRUDService;
import by.aurorasoft.replicator.v2.dto.TestV2Dto;
import by.aurorasoft.replicator.v2.service.FirstTestV2CRUDService;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.aop.framework.AopProxyUtils.getSingletonTarget;
import static org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization;

public final class ProducingReplicationAspectTest extends AbstractSpringBootTest {

    @MockBean
    private ReplicationProducerRegistry mockedProducerRegistry;

    @MockBean
    private SaveProducedReplicationFactory mockedSaveReplicationFactory;

    @Autowired
    private FirstTestV1CRUDService v1Service;

    @Autowired
    private FirstTestV2CRUDService v2Service;

    private MockedStatic<TransactionSynchronizationManager> mockedTransactionManager;

    @Captor
    private ArgumentCaptor<ReplicationCallback> callbackArgumentCaptor;

    @BeforeEach
    public void mockTransactionManager() {
        mockedTransactionManager = mockStatic(TransactionSynchronizationManager.class);
    }

    @AfterEach
    public void closeTransactionManager() {
        mockedTransactionManager.close();
    }

    @Test
    public void createByV1ServiceShouldBeProduced() {
        TestV1Dto givenDto = new TestV1Dto(255L);
        SaveProducedReplication givenReplication = mockSaveReplicationFor(givenDto);
        ReplicationProducer givenProducer = mockProducerFor(v1Service);

        TestV1Dto actual = v1Service.save(givenDto);
        assertSame(givenDto, actual);

        verifyProducing(givenProducer, givenReplication);
    }

    @Test
    public void createByV1ServiceShouldNotBeProducedBecauseOfNoReplicationProducerForService() {
        TestV1Dto givenDto = new TestV1Dto(255L);

        assertThrows(IllegalStateException.class, () -> v1Service.save(givenDto));
    }

    @Test
    public void createByV2ServiceShouldBeProduced() {
        TestV2Dto givenDto = new TestV2Dto(255L);
        SaveProducedReplication givenReplication = mockSaveReplicationFor(givenDto);
        ReplicationProducer givenProducer = mockProducerFor(v2Service);

        TestV2Dto actual = v2Service.save(givenDto);
        assertSame(givenDto, actual);

        verifyProducing(givenProducer, givenReplication);
    }

    @Test
    public void createByV2ServiceShouldNotBeProducedBecauseOfNoReplicationProducerForService() {
        TestV2Dto givenDto = new TestV2Dto(255L);

        assertThrows(IllegalStateException.class, () -> v2Service.save(givenDto));
    }

    @Test
    public void createAllByV1ServiceShouldBeProduced() {
        TestV1Dto firstGivenDto = new TestV1Dto(255L);
        TestV1Dto secondGivenDto = new TestV1Dto(256L);
        List<TestV1Dto> givenDtos = List.of(firstGivenDto, secondGivenDto);
        SaveProducedReplication firstGivenReplication = mockSaveReplicationFor(firstGivenDto);
        SaveProducedReplication secondGivenReplication = mockSaveReplicationFor(secondGivenDto);
        ReplicationProducer givenProducer = mockProducerFor(v1Service);

        List<TestV1Dto> actual = v1Service.saveAll(givenDtos);
        assertEquals(givenDtos, actual);

        verifyProducing(givenProducer, firstGivenReplication, secondGivenReplication);
    }

    @Test
    public void createAllByV1ServiceShouldNotBeProducedBecauseOfNoReplicationProducerForService() {
        List<TestV1Dto> givenDtos = List.of(new TestV1Dto(255L), new TestV1Dto(256L));

        assertThrows(IllegalStateException.class, () -> v1Service.saveAll(givenDtos));
    }

    @Test
    public void createAllByV2ServiceShouldBeProduced() {
        TestV2Dto firstGivenDto = new TestV2Dto(255L);
        TestV2Dto secondGivenDto = new TestV2Dto(256L);
        List<TestV2Dto> givenDtos = List.of(firstGivenDto, secondGivenDto);
        SaveProducedReplication firstGivenReplication = mockSaveReplicationFor(firstGivenDto);
        SaveProducedReplication secondGivenReplication = mockSaveReplicationFor(secondGivenDto);
        ReplicationProducer givenProducer = mockProducerFor(v2Service);

        List<TestV2Dto> actual = v2Service.saveAll(givenDtos);
        assertEquals(givenDtos, actual);

        verifyProducing(givenProducer, firstGivenReplication, secondGivenReplication);
    }

    @Test
    public void createAllByV2ServiceShouldNotBeProducedBecauseOfNoReplicationProducerForService() {
        List<TestV2Dto> givenDtos = List.of(new TestV2Dto(255L), new TestV2Dto(256L));

        assertThrows(IllegalStateException.class, () -> v2Service.saveAll(givenDtos));
    }

    @Test
    public void updateByV1ServiceShouldBeProduced() {
        TestV1Dto givenDto = new TestV1Dto(255L);
        SaveProducedReplication givenReplication = mockSaveReplicationFor(givenDto);
        ReplicationProducer givenProducer = mockProducerFor(v1Service);

        TestV1Dto actual = v1Service.update(givenDto);
        assertSame(givenDto, actual);

        verifyProducing(givenProducer, givenReplication);
    }

    @Test
    public void updateByV1ServiceShouldNotBeProducedBecauseOfNoReplicationProducerForService() {
        TestV1Dto givenDto = new TestV1Dto(255L);

        assertThrows(IllegalStateException.class, () -> v1Service.update(givenDto));
    }

    @Test
    public void updateByV2ServiceShouldBeProduced() {
        TestV2Dto givenDto = new TestV2Dto(255L);
        SaveProducedReplication givenReplication = mockSaveReplicationFor(givenDto);
        ReplicationProducer givenProducer = mockProducerFor(v2Service);

        TestV2Dto actual = v2Service.update(givenDto);
        assertSame(givenDto, actual);

        verifyProducing(givenProducer, givenReplication);
    }

    @Test
    public void updateByV2ServiceShouldNotBeProducedBecauseOfNoReplicationProducerForService() {
        TestV2Dto givenDto = new TestV2Dto(255L);

        assertThrows(IllegalStateException.class, () -> v2Service.update(givenDto));
    }

    @Test
    public void partialUpdateByV1ServiceShouldBeProduced() {
        Long givenId = 255L;
        Object givenPartial = new Object();
        SaveProducedReplication givenReplication = mockSaveReplicationFor(new TestV1Dto(givenId));
        ReplicationProducer givenProducer = mockProducerFor(v1Service);

        TestV1Dto actual = v1Service.updatePartial(givenId, givenPartial);
        TestV1Dto expected = new TestV1Dto(givenId);
        assertEquals(expected, actual);

        verifyProducing(givenProducer, givenReplication);
    }

    @Test
    public void partialUpdateByV1ServiceShouldNotBeProducedBecauseOfNoReplicationProducerForService() {
        Long givenId = 255L;
        Object givenPartial = new Object();

        assertThrows(IllegalStateException.class, () -> v1Service.updatePartial(givenId, givenPartial));
    }

    @Test
    public void partialUpdateByV2ServiceShouldBeProduced() {
        Long givenId = 255L;
        Object givenPartial = new Object();
        SaveProducedReplication givenReplication = mockSaveReplicationFor(new TestV2Dto(givenId));
        ReplicationProducer givenProducer = mockProducerFor(v2Service);

        TestV2Dto actual = v2Service.updatePartial(givenId, givenPartial);
        TestV2Dto expected = new TestV2Dto(givenId);
        assertEquals(expected, actual);

        verifyProducing(givenProducer, givenReplication);
    }

    @Test
    public void partialUpdateByV2ServiceShouldNotBeProducedBecauseOfNoReplicationProducerForService() {
        Long givenId = 255L;
        Object givenPartial = new Object();

        assertThrows(IllegalStateException.class, () -> v2Service.updatePartial(givenId, givenPartial));
    }

    @Test
    public void deleteByIdByV1ServiceShouldBeProduced() {
        Long givenId = 255L;
        ReplicationProducer givenProducer = mockProducerFor(v1Service);

        v1Service.deleteById(givenId);

        verifyProducing(givenProducer, new DeleteProducedReplication(givenId));
    }

    @Test
    public void deleteByIdByV1ServiceShouldNotBeProducedBecauseOfNoReplicationProducerForService() {
        Long givenId = 255L;

        assertThrows(IllegalStateException.class, () -> v1Service.deleteById(givenId));
    }

    @Test
    public void deleteByIdByV2ServiceShouldBeProduced() {
        Long givenId = 255L;
        ReplicationProducer givenProducer = mockProducerFor(v2Service);

        v2Service.delete(givenId);

        verifyProducing(givenProducer, new DeleteProducedReplication(givenId));
    }

    @Test
    public void deleteByIdByV2ServiceShouldNotBeProducedBecauseOfNoReplicationProducerForService() {
        Long givenId = 255L;

        assertThrows(IllegalStateException.class, () -> v2Service.delete(givenId));
    }

    @Test
    public void deleteAllByV1ServiceShouldBeProduced() {
        Long firstGivenDtoId = 255L;
        Long secondGivenDtoId = 256L;
        List<TestV1Dto> givenDtos = List.of(new TestV1Dto(firstGivenDtoId), new TestV1Dto(secondGivenDtoId));

        ReplicationProducer givenProducer = mockProducerFor(v1Service);

        v1Service.deleteAll(givenDtos);

        verifyProducing(
                givenProducer,
                new DeleteProducedReplication(firstGivenDtoId),
                new DeleteProducedReplication(secondGivenDtoId)
        );
    }

    @Test
    public void deleteAllByV1ServiceShouldNotBeProducedBecauseOfNoReplicationProducerForService() {
        List<TestV1Dto> givenDtos = List.of(new TestV1Dto(255L), new TestV1Dto(256L));

        assertThrows(IllegalStateException.class, () -> v1Service.deleteAll(givenDtos));
    }

    private SaveProducedReplication mockSaveReplicationFor(Object dto) {
        SaveProducedReplication replication = mock(SaveProducedReplication.class);
        when(mockedSaveReplicationFactory.create(eq(dto), any(JoinPoint.class))).thenReturn(replication);
        return replication;
    }

    private ReplicationProducer mockProducerFor(Object service) {
        ReplicationProducer producer = mock(ReplicationProducer.class);
        when(mockedProducerRegistry.get(same(unProxy(service)))).thenReturn(of(producer));
        return producer;
    }

    private Object unProxy(Object object) {
        return requireNonNullElse(getSingletonTarget(object), object);
    }

    private void verifyProducing(ReplicationProducer producer, ProducedReplication<?>... replications) {
        mockedTransactionManager
                .verify(() -> registerSynchronization(callbackArgumentCaptor.capture()), times(replications.length));
        List<ReplicationCallback> actual = callbackArgumentCaptor.getAllValues();
        List<ReplicationCallback> expected = createCallbacks(producer, replications);
        checkEquals(expected, actual);
    }

    private List<ReplicationCallback> createCallbacks(ReplicationProducer producer,
                                                      ProducedReplication<?>... replications) {
        return stream(replications)
                .map(replication -> new ReplicationCallback(producer, replication))
                .toList();
    }

    private void checkEquals(List<ReplicationCallback> expected, List<ReplicationCallback> actual) {
        assertEquals(expected.size(), actual.size());
        range(0, expected.size()).forEach(i -> checkEquals(expected.get(i), actual.get(i)));
    }

    private void checkEquals(ReplicationCallback expected, ReplicationCallback actual) {
        assertSame(expected.getProducer(), actual.getProducer());
        assertEquals(expected.getReplication(), actual.getReplication());
    }
}
