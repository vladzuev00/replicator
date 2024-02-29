package by.aurorasoft.replicator.aop;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
import by.aurorasoft.replicator.aop.ReplicationAspect.NoReplicationProducerException;
import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.aurorasoft.replicator.holder.KafkaReplicationProducerHolder;
import by.aurorasoft.replicator.model.replication.DeleteReplication;
import by.aurorasoft.replicator.model.replication.Replication;
import by.aurorasoft.replicator.model.replication.SaveReplication;
import by.aurorasoft.replicator.model.replication.UpdateReplication;
import by.aurorasoft.replicator.producer.KafkaReplicationProducer;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.apache.kafka.common.serialization.LongSerializer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNullElse;
import static java.util.Optional.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;
import static org.springframework.aop.framework.AopProxyUtils.getSingletonTarget;

public final class ReplicationAspectTest extends AbstractSpringBootTest {

    @MockBean
    private KafkaReplicationProducerHolder producerHolder;

    @Autowired
    private TestCRUDService service;

    @Captor
    private ArgumentCaptor<Replication<Long, TestDto>> replicationArgumentCaptor;

    @Before
    public void resetService() {
        service.reset();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void saveShouldBeReplicated() {
        final TestDto givenDto = new TestDto(255L);
        final KafkaReplicationProducer<Long, TestDto> givenProducer = mock(KafkaReplicationProducer.class);

        when(producerHolder.findByService(same(unProxy(service, TestCRUDService.class))))
                .thenReturn(Optional.of(givenProducer));

        final TestDto actual = service.save(givenDto);
        assertSame(givenDto, actual);

        verify(givenProducer, times(1)).send(replicationArgumentCaptor.capture());

        final Replication<Long, TestDto> actualReplication = replicationArgumentCaptor.getValue();
        final Replication<Long, TestDto> expectedReplication = new SaveReplication<>(givenDto);
        assertEquals(expectedReplication, actualReplication);
    }

    @Test(expected = NoReplicationProducerException.class)
    public void saveShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final TestDto givenDto = new TestDto(255L);

        when(producerHolder.findByService(same(unProxy(service, TestCRUDService.class)))).thenReturn(empty());

        service.save(givenDto);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void saveAllShouldBeReplicated() {
        final TestDto firstGivenDto = new TestDto(255L);
        final TestDto secondGivenDto = new TestDto(256L);
        final List<TestDto> givenDtos = List.of(firstGivenDto, secondGivenDto);

        final KafkaReplicationProducer<Long, TestDto> givenProducer = mock(KafkaReplicationProducer.class);

        when(producerHolder.findByService(same(unProxy(service, TestCRUDService.class))))
                .thenReturn(Optional.of(givenProducer));

        final List<TestDto> actual = service.saveAll(givenDtos);
        assertEquals(givenDtos, actual);

        verify(givenProducer, times(givenDtos.size())).send(replicationArgumentCaptor.capture());

        final List<Replication<Long, TestDto>> actualReplications = replicationArgumentCaptor.getAllValues();
        final List<Replication<Long, TestDto>> expectedReplications = List.of(
                new SaveReplication<>(firstGivenDto),
                new SaveReplication<>(secondGivenDto)
        );
        assertEquals(expectedReplications, actualReplications);
    }

    @Test(expected = NoReplicationProducerException.class)
    public void saveAllShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final List<TestDto> givenDtos = List.of(new TestDto(255L), new TestDto(256L));

        when(producerHolder.findByService(same(unProxy(service, TestCRUDService.class)))).thenReturn(empty());

        service.saveAll(givenDtos);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void updateShouldBeReplicated() {
        final TestDto givenDto = new TestDto(255L);
        final KafkaReplicationProducer<Long, TestDto> givenProducer = mock(KafkaReplicationProducer.class);

        when(producerHolder.findByService(same(unProxy(service, TestCRUDService.class))))
                .thenReturn(Optional.of(givenProducer));

        final TestDto actual = service.update(givenDto);
        assertSame(givenDto, actual);

        verify(givenProducer, times(1)).send(replicationArgumentCaptor.capture());

        final Replication<Long, TestDto> actualReplication = replicationArgumentCaptor.getValue();
        final Replication<Long, TestDto> expectedReplication = new UpdateReplication<>(givenDto);
        assertEquals(expectedReplication, actualReplication);
    }

    @Test(expected = NoReplicationProducerException.class)
    public void updateShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final TestDto givenDto = new TestDto(255L);

        when(producerHolder.findByService(same(unProxy(service, TestCRUDService.class)))).thenReturn(empty());

        service.update(givenDto);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void partialUpdateShouldBeReplicated() {
        final Long givenId = 255L;
        final TestDto givenDto = new TestDto(givenId);

        final Object givenPartial = new Object();
        final KafkaReplicationProducer<Long, TestDto> givenProducer = mock(KafkaReplicationProducer.class);

        service.setPartialUpdateResult(givenDto);
        when(producerHolder.findByService(same(unProxy(service, TestCRUDService.class))))
                .thenReturn(Optional.of(givenProducer));

        final TestDto actual = service.updatePartial(givenId, givenPartial);
        assertSame(givenDto, actual);

        verify(givenProducer, times(1)).send(replicationArgumentCaptor.capture());

        final Replication<Long, TestDto> actualReplication = replicationArgumentCaptor.getValue();
        final Replication<Long, TestDto> expectedReplication = new UpdateReplication<>(givenDto);
        assertEquals(expectedReplication, actualReplication);
    }

    @Test(expected = NoReplicationProducerException.class)
    public void partialUpdateShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final Long givenId = 255L;
        final Object givenPartial = new Object();

        when(producerHolder.findByService(same(unProxy(service, TestCRUDService.class)))).thenReturn(empty());

        service.updatePartial(givenId, givenPartial);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void deleteByIdShouldBeReplicated() {
        final Long givenId = 255L;
        final TestDto givenDto = new TestDto(givenId);

        final KafkaReplicationProducer<Long, TestDto> givenProducer = mock(KafkaReplicationProducer.class);

        service.setGetByIdResult(givenDto);
        when(producerHolder.findByService(same(unProxy(service, TestCRUDService.class))))
                .thenReturn(Optional.of(givenProducer));

        service.delete(givenId);

        verify(givenProducer, times(1)).send(replicationArgumentCaptor.capture());

        final Replication<Long, TestDto> actualReplication = replicationArgumentCaptor.getValue();
        final Replication<Long, TestDto> expectedReplication = new DeleteReplication<>(givenDto);
        assertEquals(expectedReplication, actualReplication);
    }

    @Test(expected = NoReplicationProducerException.class)
    public void deleteByIdShouldNotBeReplicatedBecauseOfNoReplicationProducerForService() {
        final Long givenId = 255L;

        when(producerHolder.findByService(same(unProxy(service, TestCRUDService.class)))).thenReturn(empty());

        service.delete(givenId);
    }

    @SuppressWarnings("SameParameterValue")
    private static <T> T unProxy(final Object proxy, final Class<T> targetType) {
        final Object target = requireNonNullElse(getSingletonTarget(proxy), proxy);
        return targetType.cast(target);
    }

    @Value
    private static class TestDto implements AbstractDto<Long> {
        Long id;
    }

    @Setter
    @Getter
    @ReplicatedService(
            producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
            topicConfig = @TopicConfig(name = "sync-dto")
    )
    static class TestCRUDService extends AbsServiceCRUD<
            Long,
            AbstractEntity<Long>,
            TestDto,
            JpaRepository<AbstractEntity<Long>, Long>
            > {
        private TestDto getByIdResult;
        private TestDto partialUpdateResult;

        public TestCRUDService() {
            super(null, null);
        }

        @Override
        public TestDto getById(final Long id) {
            return getByIdResult;
        }

        @Override
        public TestDto save(final TestDto dto) {
            return dto;
        }

        @Override
        public List<TestDto> saveAll(final Collection<TestDto> dtos) {
            return new ArrayList<>(dtos);
        }

        @Override
        public TestDto update(final TestDto dto) {
            return dto;
        }

        @Override
        public TestDto updatePartial(final Long id, final Object partial) {
            return partialUpdateResult;
        }

        @Override
        public void delete(final Long id) {

        }

        public void reset() {
            getByIdResult = null;
            partialUpdateResult = null;
        }
    }
}
