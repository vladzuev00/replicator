package by.aurorasoft.replicator.testrepository;

import by.aurorasoft.replicator.annotation.ReplicatedRepository;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.Producer;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.Topic;
import by.aurorasoft.replicator.testentity.TestEntity;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings("NullableProblems")
@ReplicatedRepository(producer = @Producer(idSerializer = LongSerializer.class), topic = @Topic(name = "test-topic"))
public class TestRepository implements JpaRepository<TestEntity, Long> {

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
    public void flush() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends TestEntity> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<TestEntity> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public TestEntity getOne(Long aLong) {
        return null;
    }

    @Override
    public TestEntity getById(Long aLong) {
        return null;
    }

    @Override
    public TestEntity getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends TestEntity> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends TestEntity> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends TestEntity> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends TestEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends TestEntity> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends TestEntity> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends TestEntity, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public Optional<TestEntity> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<TestEntity> findAll() {
        return null;
    }

    @Override
    public List<TestEntity> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(TestEntity entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends TestEntity> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<TestEntity> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<TestEntity> findAll(Pageable pageable) {
        return null;
    }
}
