package by.aurorasoft.replicator.base.v1.service;

import by.aurorasoft.replicator.base.v1.dto.TestV1Dto;
import by.aurorasoft.replicator.base.v1.entity.TestV1Entity;
import by.nhorushko.crudgeneric.mapper.AbstractMapper;
import by.nhorushko.crudgeneric.service.CrudGenericService;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public abstract class TestV1CRUDService extends CrudGenericService<
        TestV1Dto,
        TestV1Entity,
        JpaRepositoryImplementation<TestV1Entity, Long>,
        AbstractMapper<TestV1Entity, TestV1Dto>
        > {

    public TestV1CRUDService() {
        super(null, null, TestV1Dto.class, TestV1Entity.class);
    }

    @Override
    public TestV1Dto save(final TestV1Dto dto) {
        return dto;
    }

    @Override
    public List<TestV1Dto> saveAll(final List<TestV1Dto> dtos) {
        return new ArrayList<>(dtos);
    }

    @Override
    public TestV1Dto update(final TestV1Dto dto) {
        return dto;
    }

    @Override
    public TestV1Dto updatePartial(final Long id, final Object partial) {
        return new TestV1Dto(id);
    }

    @Override
    public void deleteById(final Long id) {

    }

    @Override
    public void deleteAll(final List<TestV1Dto> dtos) {

    }
}
