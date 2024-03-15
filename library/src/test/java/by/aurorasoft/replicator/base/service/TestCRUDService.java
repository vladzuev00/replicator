package by.aurorasoft.replicator.base.service;

import by.aurorasoft.replicator.base.dto.TestDto;
import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class TestCRUDService extends AbsServiceCRUD<Long, AbstractEntity<Long>, TestDto, JpaRepository<AbstractEntity<Long>, Long>> {

    public TestCRUDService() {
        super(null, null);
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
        return new TestDto(id);
    }

    @Override
    public void delete(final Long id) {

    }
}
