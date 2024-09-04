package by.aurorasoft.replicator.aop;

import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.registry.ReplicationProducerRegistry;
import by.aurorasoft.replicator.testcrud.TestDto;
import by.aurorasoft.replicator.testcrud.TestEntity;
import by.aurorasoft.replicator.testcrud.TestRepository;
import by.aurorasoft.replicator.testcrud.TestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public final class ProducingReplicationAspectTest extends AbstractSpringBootTest {

    @MockBean
    private ReplicationProducerRegistry mockedProducerRegistry;

    @MockBean
    private TestRepository mockedRepository;

    @Autowired
    private TestService service;

    @Test
    public void saveShouldBeProduced() {
        TestDto givenDto = TestDto.builder()
                .id(255L)
                .build();
        ReplicationProducer givenProducer = mockProducerForService();

        TestDto actual = service.save(givenDto);
        assertSame(givenDto, actual);

        verify(givenProducer, times(1)).produceSaveAfterCommit(same(givenDto));
        verifyNoInteractions(mockedRepository);
    }

    @Test
    public void saveShouldNotBeProducedBecauseOfNoProducer() {
        TestDto givenDto = TestDto.builder()
                .id(255L)
                .build();

        assertThrows(IllegalStateException.class, () -> service.save(givenDto));
    }

    @Test
    public void saveAllShouldBeProduced() {
        TestDto firstGivenDto = TestDto.builder()
                .id(255L)
                .build();
        TestDto secondGivenDto = TestDto.builder()
                .id(256L)
                .build();
        List<TestDto> givenDtos = List.of(firstGivenDto, secondGivenDto);
        ReplicationProducer givenProducer = mockProducerForService();

        List<TestDto> actual = service.saveAll(givenDtos);
        assertEquals(givenDtos, actual);

        verify(givenProducer, times(1)).produceSaveAfterCommit(same(firstGivenDto));
        verify(givenProducer, times(1)).produceSaveAfterCommit(same(secondGivenDto));
        verifyNoInteractions(mockedRepository);
    }

    @Test
    public void saveAllShouldNotBeProducedBecauseOfNoProducer() {
        TestDto firstGivenDto = TestDto.builder()
                .id(255L)
                .build();
        TestDto secondGivenDto = TestDto.builder()
                .id(256L)
                .build();
        List<TestDto> givenDtos = List.of(firstGivenDto, secondGivenDto);

        assertThrows(IllegalStateException.class, () -> service.saveAll(givenDtos));
    }

    @Test
    public void deleteByIdShouldBeProduced() {
        Long givenId = 255L;
        ReplicationProducer givenProducer = mockProducerForService();

        service.deleteById(givenId);

        verify(givenProducer, times(1)).produceDeleteAfterCommit(same(givenId));
        verifyNoInteractions(mockedRepository);
    }

    @Test
    public void deleteByIdShouldNotBeProducedBecauseOfNoProducer() {
        Long givenId = 255L;

        assertThrows(IllegalStateException.class, () -> service.deleteById(givenId));
    }

    @Test
    public void deleteShouldBeProduced() {
        Long givenId = 255L;
        TestDto givenDto = TestDto.builder()
                .id(givenId)
                .build();
        ReplicationProducer givenProducer = mockProducerForService();

        service.delete(givenDto);

        verify(givenProducer, times(1)).produceDeleteAfterCommit(same(givenId));
        verifyNoInteractions(mockedRepository);
    }

    @Test
    public void deleteShouldNotBeProducedBecauseOfNoProducer() {
        TestDto givenDto = TestDto.builder()
                .id(255L)
                .build();

        assertThrows(IllegalStateException.class, () -> service.delete(givenDto));
    }

    @Test
    public void deleteByIdsShouldBeProduced() {
        Long firstGivenId = 255L;
        Long secondGivenId = 256L;
        Long thirdGivenId = 257L;
        Iterable<Long> givenIds = List.of(firstGivenId, secondGivenId, thirdGivenId);
        ReplicationProducer givenProducer = mockProducerForService();

        service.deleteByIds(givenIds);

        verify(givenProducer, times(1)).produceDeleteAfterCommit(same(firstGivenId));
        verify(givenProducer, times(1)).produceDeleteAfterCommit(same(secondGivenId));
        verify(givenProducer, times(1)).produceDeleteAfterCommit(same(thirdGivenId));
        verifyNoInteractions(mockedRepository);
    }

    @Test
    public void deleteByIdsShouldNotBeProducedBecauseOfNoProducer() {
        Iterable<Long> givenIds = List.of(255L, 256L, 257L);

        assertThrows(IllegalStateException.class, () -> service.deleteByIds(givenIds));
    }

    @Test
    public void deleteIterableShouldBeProduced() {
        Long firstGivenId = 255L;
        Long secondGivenId = 256L;
        Long thirdGivenId = 257L;
        Iterable<TestDto> givenDtos = List.of(
                TestDto.builder()
                        .id(firstGivenId)
                        .build(),
                TestDto.builder()
                        .id(secondGivenId)
                        .build(),
                TestDto.builder()
                        .id(thirdGivenId)
                        .build()
        );
        ReplicationProducer givenProducer = mockProducerForService();

        service.delete(givenDtos);

        verify(givenProducer, times(1)).produceDeleteAfterCommit(same(firstGivenId));
        verify(givenProducer, times(1)).produceDeleteAfterCommit(same(secondGivenId));
        verify(givenProducer, times(1)).produceDeleteAfterCommit(same(thirdGivenId));
        verifyNoInteractions(mockedRepository);
    }

    @Test
    public void deleteIterableShouldNotBeProducedBecauseOfNoProducer() {
        Iterable<TestDto> givenDtos = List.of(
                TestDto.builder()
                        .id(255L)
                        .build(),
                TestDto.builder()
                        .id(256L)
                        .build(),
                TestDto.builder()
                        .id(257L)
                        .build()
        );

        assertThrows(IllegalStateException.class, () -> service.delete(givenDtos));
    }

    @Test
    public void deleteAllShouldBeProduced() {
        Long firstGivenId = 255L;
        Long secondGivenId = 256L;
        Long thirdGivenId = 257L;
        List<TestEntity> givenEntities = List.of(
                TestEntity.builder()
                        .id(firstGivenId)
                        .build(),
                TestEntity.builder()
                        .id(secondGivenId)
                        .build(),
                TestEntity.builder()
                        .id(thirdGivenId)
                        .build()
        );
        when(mockedRepository.findAll()).thenReturn(givenEntities);
        ReplicationProducer givenProducer = mockProducerForService();

        service.deleteAll();

        verify(givenProducer, times(1)).produceDeleteAfterCommit(same(firstGivenId));
        verify(givenProducer, times(1)).produceDeleteAfterCommit(same(secondGivenId));
        verify(givenProducer, times(1)).produceDeleteAfterCommit(same(thirdGivenId));
    }

    @Test
    public void deleteAllShouldNotBeProducedBecauseOfNoProducer() {
        assertThrows(IllegalStateException.class, () -> service.deleteAll());
    }

    private ReplicationProducer mockProducerForService() {
        ReplicationProducer producer = mock(ReplicationProducer.class);
        when(mockedProducerRegistry.get(same(service))).thenReturn(Optional.of(producer));
        return producer;
    }
}
