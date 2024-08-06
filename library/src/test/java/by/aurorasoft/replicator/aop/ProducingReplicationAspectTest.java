package by.aurorasoft.replicator.aop;

import by.aurorasoft.replicator.aop.ProducingReplicationAspect.ReplicationCallback;
import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.aurorasoft.replicator.producer.SaveReplicationProducer;
import by.aurorasoft.replicator.registry.DeleteReplicationProducerRegistry;
import by.aurorasoft.replicator.registry.SaveReplicationProducerRegistry;
import lombok.Value;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;
import static org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization;

@Import(
        {
                ProducingReplicationAspectTest.FirstTestRepository.class,
                ProducingReplicationAspectTest.SecondTestRepository.class
        }
)
public final class ProducingReplicationAspectTest extends AbstractSpringBootTest {

    @MockBean
    private SaveReplicationProducerRegistry mockedSaveProducerRegistry;

    @MockBean
    private DeleteReplicationProducerRegistry mockedDeleteProducerRegistry;

    @Autowired
    @Qualifier("firstTestRepository")
    private JpaRepository<TestEntity, Long> firstTestRepository;

    @Autowired
    @Qualifier("secondTestRepository")
    private JpaRepository<TestEntity, Long> secondTestRepository;

    private MockedStatic<TransactionSynchronizationManager> mockedTransactionManager;

    @Captor
    private ArgumentCaptor<ReplicationCallback> callbackCaptor;

    @BeforeEach
    public void mockTransactionManager() {
        mockedTransactionManager = mockStatic(TransactionSynchronizationManager.class);
    }

    @AfterEach
    public void closeTransactionManager() {
        mockedTransactionManager.close();
    }

    @Test
    public void saveShouldBeProduced() {
        TestEntity givenEntity = new TestEntity(255L);
        SaveReplicationProducer givenProducer = mockSaveProducerFor(firstTestRepository);

        TestEntity actual = firstTestRepository.save(givenEntity);
        assertSame(givenEntity, actual);

        verifyCallbacks(new ReplicationCallback(givenProducer, givenEntity));
    }

    private SaveReplicationProducer mockSaveProducerFor(JpaRepository<TestEntity, Long> repository) {
        SaveReplicationProducer producer = mock(SaveReplicationProducer.class);
        when(mockedSaveProducerRegistry.get(same(repository))).thenReturn(Optional.of(producer));
        return producer;
    }

    private void verifyCallbacks(ReplicationCallback... callbacks) {
        mockedTransactionManager.verify(() -> registerSynchronization(callbackCaptor.capture()), times(callbacks.length));
        List<ReplicationCallback> actual = callbackCaptor.getAllValues();
        List<ReplicationCallback> expected = List.of(callbacks);
        checkEquals(expected, actual);
    }

    private void checkEquals(List<ReplicationCallback> expected, List<ReplicationCallback> actual) {
        assertEquals(expected.size(), actual.size());
        range(0, expected.size()).forEach(i -> checkEquals(expected.get(i), actual.get(i)));
    }

    private void checkEquals(ReplicationCallback expected, ReplicationCallback actual) {
        assertSame(expected.getProducer(), actual.getProducer());
        assertEquals(expected.getModel(), actual.getModel());
    }

    @Value
    private static class TestEntity {
        Long id;
    }

    @SuppressWarnings("NullableProblems")
    public static abstract class TestRepository implements JpaRepository<TestEntity, Long> {
        private static final List<TestEntity> STORED_ENTITIES = List.of(
                new TestEntity(512L),
                new TestEntity(513L),
                new TestEntity(514L)
        );

        @Override
        public <S extends TestEntity> S save(S entity) {
            return entity;
        }

        @Override
        public <S extends TestEntity> S saveAndFlush(S entity) {
            return entity;
        }

        @Override
        public <S extends TestEntity> List<S> saveAll(Iterable<S> entities) {
            List<S> result = new ArrayList<>();
            entities.forEach(result::add);
            return result;
        }

        @Override
        public <S extends TestEntity> List<S> saveAllAndFlush(Iterable<S> entities) {
            return saveAll(entities);
        }

        @Override
        public void deleteById(Long id) {

        }

        @Override
        public void delete(TestEntity entity) {

        }

        @Override
        public void deleteAllById(Iterable<? extends Long> ids) {

        }

        @Override
        public void deleteAllByIdInBatch(Iterable<Long> ids) {

        }

        @Override
        public void deleteAll(Iterable<? extends TestEntity> entities) {

        }

        @Override
        public void deleteAllInBatch(Iterable<TestEntity> entities) {

        }

        @Override
        public void deleteAll() {

        }

        @Override
        public void deleteAllInBatch() {

        }

        @Override
        public List<TestEntity> findAll() {
            return STORED_ENTITIES;
        }

        @Override
        public void flush() {
            throw new UnsupportedOperationException();
        }

        @Override
        public TestEntity getOne(Long id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public TestEntity getById(Long id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public TestEntity getReferenceById(Long id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <S extends TestEntity> Optional<S> findOne(Example<S> example) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <S extends TestEntity> List<S> findAll(Example<S> example) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <S extends TestEntity> List<S> findAll(Example<S> example, Sort sort) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <S extends TestEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <S extends TestEntity> long count(Example<S> example) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <S extends TestEntity> boolean exists(Example<S> example) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <S extends TestEntity, R> R findBy(Example<S> example,
                                                  Function<FetchableFluentQuery<S>, R> queryFunction) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<TestEntity> findById(Long id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean existsById(Long id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<TestEntity> findAllById(Iterable<Long> ids) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long count() {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<TestEntity> findAll(Sort sort) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Page<TestEntity> findAll(Pageable pageable) {
            throw new UnsupportedOperationException();
        }
    }

    @Component
    public static class FirstTestRepository extends TestRepository {

    }

    @Component
    public static class SecondTestRepository extends TestRepository {

    }
}
