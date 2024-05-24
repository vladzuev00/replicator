package by.aurorasoft.replicator.v2.service;

import by.aurorasoft.replicator.v2.dto.TestV2Dto;
import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class TestV2CRUDService extends AbsServiceCRUD<
        Long,
        AbstractEntity<Long>,
        TestV2Dto,
        JpaRepository<AbstractEntity<Long>, Long>
        > {

    public TestV2CRUDService() {
        super(null, null);
    }

    @Override
    public TestV2Dto save(final TestV2Dto dto) {
        return dto;
    }

    @Override
    public List<TestV2Dto> saveAll(final Collection<TestV2Dto> dtos) {
        return new ArrayList<>(dtos);
    }

    @Override
    public TestV2Dto update(final TestV2Dto dto) {
        return dto;
    }

    @Override
    public TestV2Dto updatePartial(final Long id, final Object partial) {
        return new TestV2Dto(id);
    }

    @Override
    public void delete(final Long id) {

    }
}
