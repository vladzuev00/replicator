package by.aurorasoft.replicator.testrepository;

import by.aurorasoft.replicator.testentity.TestEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

//TODO use everywhere
@SuppressWarnings("NullableProblems")
public class TestRepository implements JpaRepository<TestEntity, Long> {
    private static final List<TestEntity> STORED_ENTITIES = List.of(
            new TestEntity(512L, "first-value", "second-value"),
            new TestEntity(513L, "third-value", "fourth-value"),
            new TestEntity(514L, "fifth-value", "sixth-value")
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
    public <S extends TestEntity, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
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
